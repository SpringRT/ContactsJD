package com.example.contacts.controllers;

import com.example.contacts.entities.Contact;
import java.sql.ResultSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class ContactController {

    private static final RowMapper<Contact> ROW_MAPPER = (ResultSet rs, int i) -> {
        return new Contact(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("address"), rs.getString("telephone"));
    };

    private final JdbcTemplate jdbcTemplate;

    public ContactController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("contact/index")
                .addObject("contacts", jdbcTemplate.query("select * from contact", ROW_MAPPER));
    }

    @GetMapping("create")
    public String create() {
        return "contact/create";
    }

    @PostMapping("create")
    public String create(Contact contact) {
        jdbcTemplate.update("insert into contact (name, email, address, telephone) values (?, ?, ?, ?)", new Object[]{contact.getName(), contact.getEmail(), contact.getAddress(), contact.getTelephone()});
        return "redirect:/";
    }

    @GetMapping("edit/{id}")
    public ModelAndView edit(@PathVariable int id) {
        return new ModelAndView("contact/edit")
                .addObject("contact", jdbcTemplate.queryForObject("select * from contact where id = ?", new Object[]{id}, ROW_MAPPER));
    }

    @PostMapping("edit/{id}")
    public String edit(@PathVariable int id, Contact contact) {
        jdbcTemplate.update("update contact set name = ?, email = ?, address = ?, telephone = ? where id = ?", new Object[]{contact.getName(), contact.getEmail(), contact.getAddress(), contact.getTelephone(), id});
        return "redirect:/";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id) {
        jdbcTemplate.update("delete from contact where id = ?", id);
        return "redirect:/";
    }
}
