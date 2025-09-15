package com.caloteiros.caloteiro.domain.services;

import com.caloteiros.caloteiro.application.dto.CaloteiroPageDTO;
import com.caloteiros.caloteiro.application.dto.CreateCaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.UpdateCaloteiroDTO;
import com.caloteiros.caloteiro.application.mapper.CaloteiroMapper;
import com.caloteiros.caloteiro.domain.entities.Caloteiro;
import com.caloteiros.caloteiro.domain.exceptions.CaloteiroException;
import com.caloteiros.caloteiro.domain.repositories.CaloteiroRepository;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.exceptions.UserException;
import com.caloteiros.user.domain.repositories.UserRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CaloteiroService {

    private final CaloteiroRepository caloteiroRepository;
    private final CaloteiroMapper caloteiroMapper;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CaloteiroService.class);

    private static final Set<String> VALID_SORT_FIELDS = Set.of("name", "debt", "debtDate");

    public CaloteiroService(CaloteiroRepository caloteiroRepository, UserRepository userRepository, CaloteiroMapper caloteiroMapper) {
        this.caloteiroRepository = caloteiroRepository;
        this.userRepository = userRepository;
        this.caloteiroMapper = caloteiroMapper;
    }

    @Cacheable(value = "caloteiros_listByUser",
            key = "#userId + ':' + #pageNumber + ':' + #pageSize + ':' + #sortField + ':' + #sortOrder")
    public CaloteiroPageDTO listByUser(
            Long userId,
            @PositiveOrZero int pageNumber,
            @Positive @Max(100) int pageSize,
            String sortField,
            String sortOrder) {

        logger.info("Listando caloteiros para o usuário ID: {}", userId);

        logger.debug("Parâmetros de paginação: pageNumber={}, pageSize={}, sortField={}, sortOrder={}",
                pageNumber, pageSize, sortField, sortOrder);

        Sort sort = getSort(sortField, sortOrder);

        Page<Caloteiro> page = caloteiroRepository.findByUserId(
                userId, PageRequest.of(pageNumber, pageSize, sort));

        List<CaloteiroDTO> caloteiros = page.stream()
                .map(caloteiroMapper::toCaloteiroDTO)
                .toList();

        logger.info("Encontrados {} caloteiros na página {} para o usuário ID: {}",
                page.getNumberOfElements(), pageNumber, userId);
        return caloteiroMapper.getCaloteiroPageDTO(caloteiros, page);
    }

    public CaloteiroPageDTO listByUser(
            @PositiveOrZero int pageNumber,
            @Positive @Max(100) int pageSize,
            String sortField,
            String sortOrder) {

        Long userId = getCurrentUserId();

        return listByUser(userId, pageNumber, pageSize, sortField, sortOrder);
    }

    @Cacheable(value = "caloteiros", key = "#userId + ':' + #id")
    public CaloteiroDTO findById(Long id, Long userId) {

        logger.info("Buscando caloteiro ID: {} para o usuário ID: {}", id, userId);


        Caloteiro caloteiro = caloteiroRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    logger.warn("Caloteiro com ID: {} não encontrado para o usuário ID: {}", id, userId);
                    return new CaloteiroException("Caloteiro não encontrado com o ID: " + id);
                });

        logger.info("Caloteiro ID: {} encontrado com sucesso.", id);
        return caloteiroMapper.toCaloteiroDTO(caloteiro);
    }

    public CaloteiroDTO findById(Long id) {
        Long userId = getCurrentUserId();
        return findById(id, userId);
    }

    @Cacheable(value = "caloteiros_search",
            key = "#userId + ':' + #name + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public CaloteiroPageDTO searchByName(String name, Pageable pageable, Long userId) {

        logger.info("Buscando caloteiros por nome contendo '{}' para o usuário ID: {}", name, userId);

        Page<Caloteiro> page = caloteiroRepository.findByNameContainingIgnoreCaseAndUserId(name, userId, pageable);

        logger.debug("Parâmetros de busca: name={}, pageNumber={}, pageSize={}, sort={}, userId={}",
                name, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort(), userId);

        List<CaloteiroDTO> caloteiros = page.getContent()
                .stream()
                .map(caloteiroMapper::toCaloteiroDTO)
                .toList();

        logger.info("Busca por '{}' encontrou {} caloteiros na página {} para o usuário ID: {}",
                name, page.getNumberOfElements(), page.getNumber(), userId);

        return caloteiroMapper.getCaloteiroPageDTO(caloteiros, page);
    }

    public CaloteiroPageDTO searchByName(String name, Pageable pageable) {
        Long userId = getCurrentUserId();
        return searchByName(name, pageable, userId);
    }

    @CacheEvict(value = {"caloteiros", "caloteiros_listByUser", "caloteiros_search"}, allEntries = true)
    @Transactional
    public CaloteiroDTO create(Long userId, CreateCaloteiroDTO createCaloteiroDTO) {

        logger.info("Iniciando criação de caloteiro para o usuário ID: {}", userId);

        logger.debug("Dados recebidos para criação: {}", createCaloteiroDTO);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Falha na criação de caloteiro: Usuário com ID {} não encontrado.", userId);
                    return new UserException("Usuário não encontrado");
                });

        Caloteiro caloteiro = caloteiroMapper.fromCreateDTOToEntity(createCaloteiroDTO);
        caloteiro.setUser(user);

        caloteiroRepository.save(caloteiro);

        logger.info("Caloteiro '{}' criado com sucesso com ID: {} para o usuário ID: {}",
                caloteiro.getName(), caloteiro.getId(), userId);
        return caloteiroMapper.toCaloteiroDTO(caloteiro);
    }

    public CaloteiroDTO create(CreateCaloteiroDTO createCaloteiroDTO) {
        Long userId = getCurrentUserId();
        return create(userId, createCaloteiroDTO);
    }

    @CacheEvict(value = {"caloteiros", "caloteiros_listByUser", "caloteiros_search"}, key = "#userId + ':' + #id")
    @Transactional
    public void delete(Long id, Long userId) {

        logger.info("Iniciandi exclusão do caloteiro ID: {} pelo usuário ID: {}", id, userId);

        Caloteiro caloteiro = caloteiroRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    logger.warn("Falha na exclusão: Caloteiro ID {} não pertence ou não existe para o usuário ID: {}", id, userId);
                    return new CaloteiroException("Não foi possível excluir o Caloteiro");
                });

        caloteiroRepository.delete(caloteiro);

        logger.info("Caloteiro ID: {} excluído com sucesso.", id);
    }

    public void delete(Long id) {
        Long userId = getCurrentUserId();
        delete(id, userId);
    }

    @CacheEvict(value = {"caloteiros", "caloteiros_listByUser", "caloteiros_search"}, key = "#userId + ':' + #id")
    @Transactional
    public void update(Long id, Long userId, UpdateCaloteiroDTO updateCaloteiroDTO) {

        logger.info("Iniciando atualização do caloteiro ID: {} pelo usuário ID: {}", id, userId);

        logger.debug("Dados recebidos para atualização: {}", updateCaloteiroDTO);

        Caloteiro caloteiro = caloteiroRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    logger.warn("Falha na atualização: Caloteiro ID {} não pertence ou não existe para o usuário ID: {}", id, userId);
                    return new CaloteiroException("Não foi possível atualizar o caloteiro");
                });

        caloteiro = caloteiroMapper.fromUpdateDTOToEntity(updateCaloteiroDTO, caloteiro);
        caloteiroRepository.save(caloteiro);

        logger.info("Caloteiro ID: {} atualizado com sucesso.", id);
    }

    public void update(Long id, UpdateCaloteiroDTO updateCaloteiroDTO) {
        Long userId = getCurrentUserId();
        update(id, userId, updateCaloteiroDTO);
    }

    private Sort getSort(String sortField, String sortOrder) {
        if (!VALID_SORT_FIELDS.contains(sortField)) {
            logger.warn("Tentativa de ordenação por campo inválido: '{}'. Campos válidos: {}", sortField, VALID_SORT_FIELDS);
            throw new CaloteiroException("Campo de orndeção inválido: " + sortField);
        }
        Sort sort = Sort.by(sortField);
        return "desc".equalsIgnoreCase(sortOrder) ? sort.descending() : sort.ascending();
    }

    private Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        logger.debug("Tentando obter ID do usuário logado. Principal: {}, Nome de autenticação: {},",
                principal.getClass().getSimpleName(), auth.getName());

        if (principal instanceof User u && u.getId() != null) {
            logger.debug("ID do usuário obtido diretamente do objeto User principal: {}", u.getId());
            return u.getId();
        }

        String login = auth.getName();
        Long id = userRepository.findIdByEmail(login);
        if (id != null) {
            logger.debug("ID do usuário obtido via e-mail '{}': {}", login, id);
            return id;
        }

        id = userRepository.findIdByUsername(login);
        if (id != null) {
            logger.debug("ID do usuário obtido via username '{}': {}", login, id);
            return id;
        }

        logger.error("Não foi possível encontrar o ID do usuário para o identificador: {}", login);
        throw new UserException("Usuário não encontrado para o identificador: " + login);
    }
}