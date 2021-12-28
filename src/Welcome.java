import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
@ManagedBean(name = "Welcome")
@RequestScoped
public class Welcome implements Serializable {

	public static final String welcomeTitle = "Welcome to Online Examination Project";
}
