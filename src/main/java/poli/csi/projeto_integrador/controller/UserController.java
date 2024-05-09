package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filter.FilterUser;
import poli.csi.projeto_integrador.dto.request.UpdateUserDto;
import poli.csi.projeto_integrador.dto.request.SaveUserDto;
import poli.csi.projeto_integrador.model.User;
import poli.csi.projeto_integrador.service.UserService;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody SaveUserDto dto) {
        boolean res = userService.save(dto);
        if (res) {
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao cadastrar usuário!");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserDto dto) {
        boolean res = userService.update(dto);
        if (res) {
            return ResponseEntity.ok("Dados do usuário alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar dados do usuário!");
    }

    @PutMapping("/update/status/{id}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("id") Long id) {
        boolean res = userService.updateStatus(id);
        if (res) {
            return ResponseEntity.ok("Status do usuário alterado com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar status do usuário!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User res = userService.getUser(id);
        if (res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "active", required = false) Boolean active
    ) {
        FilterUser filter = new FilterUser(search, role, active);
        Page<User> res = userService.getUsers(pageable, filter);
        if (res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }
}
