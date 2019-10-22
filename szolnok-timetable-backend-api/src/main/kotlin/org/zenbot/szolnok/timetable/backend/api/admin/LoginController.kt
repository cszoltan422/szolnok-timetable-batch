package org.zenbot.szolnok.timetable.backend.api.admin

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class LoginController {

    @RequestMapping("/login")
    fun loginpage() : ModelAndView {
        return ModelAndView("/login")
    }
}