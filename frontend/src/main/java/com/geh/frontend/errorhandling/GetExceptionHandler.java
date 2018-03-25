/**
 * 
 */
package com.geh.frontend.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.geh.frontend.controllers.FrontendException;

/**
 * Exception handling of internal application exceptions
 *
 * @author Vera Boutchkova
 */
@ControllerAdvice
public class GetExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({ Exception.class, FrontendException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected String exception(final Exception ex, final Model model) {
		super.logger.error("[GEH][ErrorHandling] Exception occurred", ex);
		String errorMessage = (ex != null ? ex.getMessage() : "Unknown error occurred");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
	}
}