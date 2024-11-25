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
    private final UserRepository userRepository;
    private final CaloteiroMapper caloteiroMapper;

    private static final Set<String> VALID_SORT_FIELDS = Set.of("name", "debt", "debtDate");

    public CaloteiroService(CaloteiroRepository caloteiroRepository, UserRepository userRepository, CaloteiroMapper caloteiroMapper) {
        this.caloteiroRepository = caloteiroRepository;
        this.userRepository = userRepository;
        this.caloteiroMapper = caloteiroMapper;
    }

    @Cacheable(value = "caloteiros_listByUser",
            key = "T(java.util.Objects).hash(#pageNumber, #pageSize, #sortField, #sortOrder, #userEmail)")
    public CaloteiroPageDTO listByUser(
            @PositiveOrZero int pageNumber,
            @Positive @Max(100) int pageSize,
            String sortField,
            String sortOrder) {

        Sort sort = getSort(sortField, sortOrder);

        String userEmail = getCurrentUserEmail();

        Page<Caloteiro> page = caloteiroRepository.findByUserEmail(
                userEmail, PageRequest.of(pageNumber, pageSize, sort));

        List<CaloteiroDTO> caloteiros = page.get()
                                            .map(caloteiroMapper::toCaloteiroDTO)
                                            .toList();

         return caloteiroMapper.getCaloteiroPageDTO(caloteiros, page);
    }

    @Cacheable(value = "caloteiros", key = "#id")
    public CaloteiroDTO findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return caloteiroRepository.findByIdAndUserUsername(id, username)
                .map(caloteiroMapper::toCaloteiroDTO)
                .orElseThrow(() -> new CaloteiroException("Caloteiro não encontrado com o ID: " + id));
    }

    @Cacheable(value = "caloteiros_search", key = "#name + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public CaloteiroPageDTO searchByName(String name, Pageable pageable) {
        Page<Caloteiro> page = caloteiroRepository.findByNameContainingIgnoreCase(name, pageable);

        List<CaloteiroDTO> caloteiros = page.getContent()
                .stream()
                .map(caloteiroMapper::toCaloteiroDTO)
                .toList();

        return caloteiroMapper.getCaloteiroPageDTO(caloteiros, page);
    }

    @CacheEvict(value = {"caloteiros_listByUser", "caloteiros_search"}, allEntries = true)
    @Transactional
    public void create(CreateCaloteiroDTO createCaloteiroDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();
        String email = userPrincipal.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuário não encontrado"));

        Caloteiro caloteiro = caloteiroMapper.fromCreateDTOToEntity(createCaloteiroDTO);
        caloteiro.setUser(user);

        caloteiroRepository.save(caloteiro);
    }

    @CacheEvict(value = {"caloteiros", "caloteiros_listByUser", "caloteiros_search"}, key = "#id")
    @Transactional
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Caloteiro caloteiro = caloteiroRepository.findByIdAndUserUsername(id, username)
                        .orElseThrow(() -> new CaloteiroException("Não foi possível excluir o Caloteiro"));

        caloteiroRepository.delete(caloteiro);
    }

    @CacheEvict(value = {"caloteiros", "caloteiros_listByUser", "caloteiros_search"}, key = "#id")
    @Transactional
    public void update(Long id, UpdateCaloteiroDTO updateCaloteiro) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();


        Caloteiro caloteiro = caloteiroRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new CaloteiroException("Não foi possível atualizar o caloteiro"));

        caloteiro = caloteiroMapper.fromUpdateDTOToEntity(updateCaloteiro, caloteiro);
        caloteiroRepository.save(caloteiro);
    }

    private Sort getSort(String sortField, String sortOrder) {
        if (!VALID_SORT_FIELDS.contains(sortField)) {
            throw new IllegalArgumentException(("Campo de orndeção inválido: " + sortField));
        }
        Sort sort = Sort.by(sortField);
        return "desc".equalsIgnoreCase(sortOrder) ? sort.descending() : sort.ascending();
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();
        return userPrincipal.getEmail();
    }
}
