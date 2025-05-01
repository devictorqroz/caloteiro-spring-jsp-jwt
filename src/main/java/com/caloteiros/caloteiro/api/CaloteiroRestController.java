package com.caloteiros.caloteiro.api;

import com.caloteiros.caloteiro.application.dto.CaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.CaloteiroPageDTO;
import com.caloteiros.caloteiro.application.dto.CreateCaloteiroDTO;
import com.caloteiros.caloteiro.application.dto.UpdateCaloteiroDTO;
import com.caloteiros.caloteiro.domain.services.CaloteiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/caloteiros")
@Tag(name = "Caloteiros", description = "Operações da API de caloteiros")
public class CaloteiroRestController {

    private final CaloteiroService caloteiroService;

    public CaloteiroRestController(CaloteiroService service) {
        this.caloteiroService = service;
    }



    @Operation(
            summary = "Listar caloteiros do usuário logado com paginação e ordenação",
            description = "Retorna uma página com os caloteiros cadastrados pelo usuário autenticado",
            responses = {
                @ApiResponse(
                    responseCode = "200",
                    description = "Página de caloteiros retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CaloteiroPageDTO.class))
                ),
                @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @GetMapping
    public CaloteiroPageDTO listByUser(
            @Parameter(description = "Número da página (0 = primeira)", example = "0")
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,

            @Parameter(description = "Quantidade de elementos por página", example = "10")
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize,

            @Parameter(description = "Campo de ordenação", example = "name")
            @RequestParam(defaultValue = "name") String sortField,

            @Parameter(description = "Direção da ordenação (asc ou desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        return caloteiroService.listByUser(page, pageSize, sortField, sortOrder);
    }



    @Operation(
        summary = "Buscar caloteiro por ID",
        description = "Retorna os dados detalhados de um caloteiro específico pertencente ao usuário autenticado",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Caloteiro encontrado com sucesso",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CaloteiroDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Caloteiro não encontrado")
        }
    )
    @GetMapping("/{id}")
    public CaloteiroDTO findById(
            @Parameter(description = "ID do caloteiro a ser buscado", example = "1")
            @PathVariable Long id
    ) {
        return caloteiroService.findById(id);
    }



    @Operation(
            summary = "Cadastrar novo caloteiro",
            description = "Registra um novo caloteiro vinculado ao usuário autenticado",
            responses = {
                @ApiResponse(
                    responseCode = "201",
                    description = "Caloteiro cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CaloteiroDTO.class))
                ),
                @ApiResponse(responseCode = "400", description = "Requisição inválida (dados com erro)"),
                @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PostMapping
    public ResponseEntity<CaloteiroDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "Dados do caloteiro a ser cadastrado",
                content = @Content(schema = @Schema(implementation = CreateCaloteiroDTO.class))
            )
            @Valid @RequestBody CreateCaloteiroDTO dto
    ) {
        CaloteiroDTO created = caloteiroService.create(dto);
        return ResponseEntity.status(201).body(created);
    }



    @Operation(
            summary = "Atualizar caloteiro existente",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Caloteiro atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida"),
                    @ApiResponse(responseCode = "401", description = "Não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
                    @ApiResponse(responseCode = "404", description = "Caloteiro não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Parameter(description = "ID do caloteiro a ser atualizado", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados atualizados do caloteiro",
                required = true,
                content = @Content(schema = @Schema(implementation = UpdateCaloteiroDTO.class))
            )
            @Valid @RequestBody UpdateCaloteiroDTO dto
    ) {
        caloteiroService.update(id, dto);
        return ResponseEntity.noContent().build();
    }



    @Operation(
            summary = "Remover caloteiro por ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Caloteiro deletado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida"),
                    @ApiResponse(responseCode = "401", description = "Não autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
                    @ApiResponse(responseCode = "404", description = "Caloteiro não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do caloteiro a ser removido", example = "1")
            @PathVariable Long id
    ) {
        caloteiroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}