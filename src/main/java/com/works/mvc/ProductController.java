package com.works.mvc;

import com.works.dto.ProductUpdateRequestDto;
import com.works.entity.Product;
import com.works.repository.ProductRepository;
import com.works.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("mvc")
@RequiredArgsConstructor
public class ProductController {

    final HttpServletRequest request;
    final ProductService productService;
    final ProductRepository productRepository;

    @GetMapping("product")
    public String dashboard(Model model, @RequestParam(defaultValue = "1") int page) {
        Page<Product> productPage = productService.productList(page-1);
        model.addAttribute("productPage", productPage);
        return "product";
    }

    @GetMapping("product/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteOne(id);
        return "redirect:/mvc/product";
    }

    @GetMapping("product/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        request.getSession().setAttribute("selectId", id);
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        return "productUpdate";
    }

    @PostMapping("product/update")
    public String update(ProductUpdateRequestDto productUpdateRequestDto) {
        Long selectId = (long) request.getSession().getAttribute("selectId");
        productUpdateRequestDto.setId(selectId);
        productService.update(productUpdateRequestDto);
        return "redirect:/mvc/product";
    }

    @GetMapping("product/search")
    public String search(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "") String q) {
        Page<Product> productPage = productService.search(q, page-1, "asc");
        model.addAttribute("productPage", productPage);
        model.addAttribute("q", q);
        return "product";
    }

}
