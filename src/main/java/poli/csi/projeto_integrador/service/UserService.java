package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filter.FilterUser;
import poli.csi.projeto_integrador.dto.request.UpdateUserDto;
import poli.csi.projeto_integrador.dto.request.SaveUserDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.User;
import poli.csi.projeto_integrador.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nome de usuário ou senha incorretos!"));

        if(user != null) {
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRole().name()).build();

            return userDetails;
        }
        return null;
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado no sistema!"));
    }

    @Transactional
    public boolean save(SaveUserDto dto) {
        User isExist = userRepository.findUserByEmailOrUsername(dto.email().trim(), dto.username().trim())
                .orElse(null);

        if(isExist != null) {
            throw new CustomException("E-mail ou Nome de usuário já em uso no sistema!");
        }


        User user = new User();
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String pass = bcrypt.encode(dto.password().trim());

        user.setName(dto.name().toLowerCase().trim());
        user.setEmail(dto.email().trim());
        user.setPhone(dto.phone().trim());
        user.setUsername(dto.username().trim());
        user.setPassword(pass);
        user.setSiape(dto.siape().trim());
        user.setActive(true);
        user.setRole(User.UserType.SERVIDOR);

        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean update(UpdateUserDto dto) {
        User user = userRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        User isExist = userRepository.findUserByEmailOrUsername(dto.email().trim(), dto.username().trim())
                .orElse(null);

        if(isExist != null && !isExist.equals(user)) {
            throw new CustomException("E-mail ou Nome de usuário já em uso no sistema!");
        }

        user.setName(dto.name().toLowerCase().trim());
        user.setEmail(dto.email().trim());
        user.setPhone(dto.phone().trim());

        if(dto.username() != null) {
            user.setUsername(dto.username().trim());
        }

        if(dto.password() != null) {
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            String pass = bcrypt.encode(dto.password().trim());
            user.setPassword(pass);
        }

        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean updateStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));


        user.setActive(!user.getActive());

        userRepository.save(user);
        return true;
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));
    }

    public Page<User> getUsers(Pageable pageable, FilterUser filters) {
        if(isFilter(filters)) {
            Specification<User> spec = UserRepository.specUser(filters);
            return userRepository.findAll(spec, pageable);
        }
        return userRepository.findAll(pageable);
    }

    private boolean isFilter(FilterUser f) {
        return f.search() != null ||
                f.role() != null ||
                f.active() != null;
    }
}
