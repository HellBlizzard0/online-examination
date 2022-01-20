package com.beans;

import java.util.concurrent.TimeUnit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ViewScoped
@ManagedBean(name = "timerBean")
public class TimerBean {
	private int timeInMin;
	private int remaining;
	private boolean stop = true;
	private String percentage = "100";
	private String caption = "Begin";
	private String look = "primary";

	public String getLook() {
		return look;
	}

	public void setLook(String look) {
		this.look = look;
	}

	public TimerBean(int timeInMin) {
		super();
		this.timeInMin = timeInMin;
		this.remaining = this.timeInMin;
	}

	public TimerBean() {
		super();
		this.timeInMin = 30 * 60;
		this.remaining = this.timeInMin;
	}

	public void decrement() {
		if (remaining > 0) {
			this.remaining -= 1;
			this.percentage = "" + ((int) (((double) this.remaining / this.timeInMin) * 100));
			int remaining = this.remaining*1000;
			this.caption = String.format("%02d:%02d:%02d", 
					TimeUnit.MILLISECONDS.toHours(remaining),
					TimeUnit.MILLISECONDS.toMinutes(remaining) -  
					TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(remaining)), // The change is in this line
					TimeUnit.MILLISECONDS.toSeconds(remaining) - 
					TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remaining))); 
			updateColor();
		} else {
			this.percentage = "Exam Over";
		}
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	private void updateColor() {
		// TODO Auto-generated method stub
		if (((int) (((double) this.remaining / this.timeInMin) * 100)) < 50) {
			this.look = "warning";
		}
		if (((int) (((double) this.remaining / this.timeInMin) * 100)) < 25) {
			this.look = "danger";
		}

	}

	public int getTimeInMin() {
		return timeInMin;
	}

	public void setTimeInMin(int timeInMin) {
		this.timeInMin = timeInMin;
	}

	public int getRemaining() {
		return remaining;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
}
