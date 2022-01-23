package com.converters;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.entities.Answer;
import com.entities.QuestionAnswers;


public class AnswerConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String answerId) {
		try {
			if(answerId.equals(""))
				throw new Exception("Empty answerID");
			ValueExpression vex = ctx.getApplication().getExpressionFactory().createValueExpression(ctx.getELContext(),
					"#{question}", QuestionAnswers.class);

			QuestionAnswers questions = (QuestionAnswers) vex.getValue(ctx.getELContext());

			for (Answer answer : questions.getAnswers()) {
				if (answer.toString().equals(answerId)) {
					questions.setSelectedAnswer(answer);
					return answer;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return new Answer();
	}

	@Override
	public String getAsString(FacesContext context, 
			UIComponent component, 
			Object answer) {
		System.out.println();
		return ((Answer) answer).getText();
	}

}
