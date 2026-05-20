package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.UsuarioServiceAPI;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/inicio")
public class WebController {
    
    @Autowired
    private UsuarioServiceAPI usuarioServiceAPI;
    
    @GetMapping()
    public String root() {
        return "redirect:/api/inicio/login";
    }
    
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpSession session, 
                       Model model) {
        return usuarioServiceAPI.findByUsernameAndPassword(username, password)
                .map(usuario -> {
                    session.setAttribute("usuario", usuario);
                    return "redirect:/api/inicio/principal";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Usuario o contraseña incorrectos");
                    return "login";
                });
    }
    
    @GetMapping("/principal")
    public String principal(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/api/inicio/login";
        model.addAttribute("usuario", usuario);
        return "principal";
    }
    
    @GetMapping("/usuarios")
    public String usuarios(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/api/inicio/login";
        model.addAttribute("usuarios", usuarioServiceAPI.getAll());
        return "usuarios";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/api/inicio/login";
    }
}