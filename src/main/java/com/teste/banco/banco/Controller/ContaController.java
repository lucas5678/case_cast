package com.teste.banco.banco.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.teste.banco.banco.DTO.ModelContaDTO;
import com.teste.banco.banco.DTO.ModeloTransferDTO;
import com.teste.banco.banco.Models.ModelConta;
import com.teste.banco.banco.Services.ContaMovimentacao;
import com.teste.banco.banco.Services.ContaService;
import com.teste.banco.banco.Services.ContaTransfer;

import org.springframework.ui.Model;

@Controller
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ContaMovimentacao contaMovimentacao;

    @Autowired
    private ContaTransfer contaTransfer;

    @GetMapping("/admin")
    public String Admin(Model model) {
        model.addAttribute("conta", new ModelConta());
        return "admin";
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
            contaMovimentacao.AdicionarCredito(conta);
            redirectAttributes.addFlashAttribute("mensagem", "Valor adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/view-add-credito";
    }

    @PostMapping("/debitar-credito")
    public String DebitarCredito(@ModelAttribute ModelConta conta, RedirectAttributes redirectAttributes) {
        try {
            contaMovimentacao.DebitarCredito(conta);
            redirectAttributes.addFlashAttribute("mensagem", "Valor debitado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/view-debitar-credito";
    }


    @GetMapping("/view-movimentacao")
    public String ViewMovimentacao(Model model) {
        // Se já existe um atributo "conta" (via RedirectAttributes), não sobrescreva
        ModeloTransferDTO conta = new ModeloTransferDTO();
        model.addAttribute("conta", conta);
        return "movimentacao";
    }
    
    @PostMapping("/transferir")
    public String Transferir(@ModelAttribute ModeloTransferDTO conta, RedirectAttributes redirectAttributes) {
        try {
            contaTransfer.transferir(conta);
            redirectAttributes.addFlashAttribute("mensagem", "Valor transferido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            redirectAttributes.addFlashAttribute("conta", conta);
        }
        return "redirect:/view-movimentacao";
    }
}
