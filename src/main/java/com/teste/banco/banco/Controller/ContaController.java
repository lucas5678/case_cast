package com.teste.banco.banco.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.teste.banco.banco.DTO.ModelContaDTO;
import com.teste.banco.banco.DTO.ModelLoginDTO;
import com.teste.banco.banco.DTO.ModeloTransferDTO;
import com.teste.banco.banco.Models.ModelConta;
import com.teste.banco.banco.Services.ContaLogin;
import com.teste.banco.banco.Services.ContaMovimentacao;
import com.teste.banco.banco.Services.ContaService;
import com.teste.banco.banco.strategy.CreditoStrategy;
import com.teste.banco.banco.strategy.DebitoStrategy;
import com.teste.banco.banco.strategy.MovimentacaoContext;
import com.teste.banco.banco.strategy.TransferenciaStrategy;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ContaMovimentacao contaMovimentacao;

    @Autowired
    private ContaLogin contaLogin;
    
    @Autowired
    private CreditoStrategy creditoStrategy;
    
    @Autowired
    private DebitoStrategy debitoStrategy;
    
    @Autowired
    private TransferenciaStrategy transferenciaStrategy;

    @GetMapping("/admin")
    public String Admin(Model model) {
        model.addAttribute("conta", new ModelConta());
        return "admin";
    }

    @GetMapping("/user")
    public String User(Model model,HttpSession session) {
        //atualiza informações do usuario logado
        ModelLoginDTO conta = contaLogin.AtualizaInformacoes((ModelLoginDTO) session.getAttribute("conta"));
        session.setAttribute("conta", conta);
        ModelLoginDTO contaAtt = (ModelLoginDTO) session.getAttribute("conta");
        if (conta == null) {
            return "redirect:/";
        }
        model.addAttribute("conta", contaAtt);
        return "usuario";
    }

    @GetMapping("/add-credito-user")
    public String UserAddCredito(Model model,HttpSession session) {
        ModelLoginDTO conta = (ModelLoginDTO) session.getAttribute("conta");
        if (conta == null) {
            return "redirect:/";
        }
        model.addAttribute("conta", conta);
        return "addCreditoUsuario";
    }

    @GetMapping("/add-debito-user")
    public String UserAddDebito(Model model,HttpSession session) {
        ModelLoginDTO conta = (ModelLoginDTO) session.getAttribute("conta");
        if (conta == null) {
            return "redirect:/";
        }
        model.addAttribute("conta", conta);
        return "addDebitoUsuario";
    }
    
    @GetMapping("/novo")
    public String mostrarFormularioDeCadastro(Model model) {
        ModelContaDTO conta = new ModelContaDTO();
        conta.setNumeroConta(contaService.gerarNumeroConta());
        model.addAttribute("conta", conta);
        return "cadastro-clientes";
    }

    @PostMapping("/cadastro-conta")
    public String salvarConta(@ModelAttribute ModelContaDTO conta, RedirectAttributes redirectAttributes) {
        try {
            contaService.salvarConta(conta);
            redirectAttributes.addFlashAttribute("mensagem", "Conta cadastrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/novo";
    }

    @GetMapping("/view-add-credito")
    public String Creditar(Model model) {
        // Se já existe um atributo "conta" (via RedirectAttributes), não sobrescreva
        if (!model.containsAttribute("conta")) {
            model.addAttribute("conta", new ModelConta());
        }
        return "creditar";
    }

    @GetMapping("/view-debitar-credito")
    public String Debitar(Model model) {
        // Se já existe um atributo "conta" (via RedirectAttributes), não sobrescreva
        if (!model.containsAttribute("conta")) {
            model.addAttribute("conta", new ModelConta());
        }
        return "debitar";
    }

    @GetMapping("/buscar-conta")
    public String buscarConta(@RequestParam(name = "numeroConta") Integer numeroConta, RedirectAttributes redirectAttributes) {
        ModelConta conta = contaMovimentacao.buscarPorNumero(numeroConta).orElse(null);
        if (conta == null) {
            redirectAttributes.addFlashAttribute("erro", "Conta não encontrada");
        } else {
            conta.setSaldo(0.0);
            redirectAttributes.addFlashAttribute("conta", conta);
        }
        return "redirect:/view-add-credito";
    }

    @GetMapping("/buscar-conta-debito")
    public String buscarContaDebito(@RequestParam(name = "numeroConta") Integer numeroConta, RedirectAttributes redirectAttributes) {
        ModelConta conta = contaMovimentacao.buscarPorNumero(numeroConta).orElse(null);
        if (conta == null) {
            redirectAttributes.addFlashAttribute("erro", "Conta não encontrada");
        } else {
            conta.setSaldo(0.0);
            redirectAttributes.addFlashAttribute("conta", conta);
        }
        return "redirect:/view-debitar-credito";
    }

    @PostMapping("/adicionar-credito")
    public String AdicionarCredito(@ModelAttribute ModelContaDTO conta, RedirectAttributes redirectAttributes) {
        try {
            ModelConta modelConta = new ModelConta();
            modelConta.setNumeroConta(conta.getNumeroConta());
            modelConta.setSaldo(conta.getSaldo());
            
            MovimentacaoContext context = new MovimentacaoContext(creditoStrategy);
            context.executar(modelConta);
            redirectAttributes.addFlashAttribute("mensagem", "Valor adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/view-add-credito";
    }

    @PostMapping("/adicionar-credito-por-usuario")
    public String AdicionarCreditoPorUsuario(@ModelAttribute ModelContaDTO conta, RedirectAttributes redirectAttributes) {
        try {
            ModelConta modelConta = new ModelConta();
            modelConta.setNumeroConta(conta.getNumeroConta());
            modelConta.setSaldo(conta.getSaldo());
            
            MovimentacaoContext context = new MovimentacaoContext(creditoStrategy);
            context.executar(modelConta);
            redirectAttributes.addFlashAttribute("mensagem", "Valor adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/add-credito-user";
    }

    @PostMapping("/debitar-credito")
    public String DebitarCredito(@ModelAttribute ModelConta conta, RedirectAttributes redirectAttributes) {
        try {
            MovimentacaoContext context = new MovimentacaoContext(debitoStrategy);
            context.executar(conta);
            redirectAttributes.addFlashAttribute("mensagem", "Valor debitado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/view-debitar-credito";
    }

    @PostMapping("/debitar-credito-por-usuario")
    public String DebitarCreditoPorUsuario(@ModelAttribute ModelConta conta, RedirectAttributes redirectAttributes) {
        try {
            MovimentacaoContext context = new MovimentacaoContext(debitoStrategy);
            context.executar(conta);
            redirectAttributes.addFlashAttribute("mensagem", "Valor debitado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/add-debito-user";
    }

    @GetMapping("/view-transferir")
    public String ViewTransferir(Model model) {
        if (!model.containsAttribute("transferencia")) {
            model.addAttribute("transferencia", new ModeloTransferDTO());
        }
        return "transferir";
    }

    @PostMapping("/realizar-transferencia")
    public String RealizarTransferencia(@ModelAttribute ModeloTransferDTO transferencia, RedirectAttributes redirectAttributes) {
        try {
            transferenciaStrategy.executarTransferencia(transferencia);
            redirectAttributes.addFlashAttribute("mensagem", "Transferência realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/view-transferir";
    }
}