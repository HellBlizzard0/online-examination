package com.code.dal;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.servlet.ServletContext;

import org.hibernate.cfg.Configuration;

import com.code.services.log4j.Log4j;

public class CustomeAnnotationConfigurations extends Configuration {
	private static final long serialVersionUID = 1L;

	private ServletContext context;

	public CustomeAnnotationConfigurations(ServletContext sc) {
		super();
		this.context = sc;
	}

	public Configuration addPackage(String p1) {
		try {
			String ormPath = p1.replace('.', '/');
			File file = new File(context.getResource("/WEB-INF/classes" + "/" + ormPath).getFile());
			String temp = ".jar!\\";
			if (!System.getProperty("os.name").toLowerCase().contains("win"))
				temp = ".jar!/";

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + System.getProperty("os.name"));
			if (file.getAbsolutePath().contains(temp))
				addClassesToConfiguration(file);
			else
				addNestedClasses(file, ormPath.replace('/', '.'));
		} catch (Exception e) {
			Log4j.traceErrorException(CustomeAnnotationConfigurations.class, e, "CustomeAnnotationConfigurations");
		}
		return this;
	}

	private void addClassesToConfiguration(File packageFile) throws Exception {
		String[] paths = packageFile.getAbsolutePath().split(".jar!");
		String jarName = paths[0] + ".jar";
		String packageName = paths[1].replace("\\", "/").substring(1);

		File f = new File(jarName);
		if (f.exists()) {
			try {
				JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
				while (true) {
					JarEntry jarEntry = jarFile.getNextJarEntry();
					if (jarEntry == null)
						break;

					if ((jarEntry.getName().startsWith(packageName)) && jarEntry.getName().endsWith(".class")) {
						Class bean = Class.forName(jarEntry.getName().replaceAll("/", "\\.").substring(0, jarEntry.getName().length() - 6));

						this.addAnnotatedClassToConfiguration(bean);
					}
				}
			} catch (Exception e) {
				Log4j.traceErrorException(CustomeAnnotationConfigurations.class, e, "CustomeAnnotationConfigurations");
			}
		}
	}

	private void addNestedClasses(File file, String ormPath) throws Exception {
		File[] files = file.listFiles();
		for (File child : files) {
			if (child.getAbsolutePath().contains("svn") || child.getAbsolutePath().contains("DS_Store"))
				continue;
			if (child.isDirectory())
				addNestedClasses(child, ormPath + "." + child.getName());
			else {
				Class bean = Class.forName(ormPath + "." + child.getName().replaceAll(".class", ""));
				this.addAnnotatedClassToConfiguration(bean);
			}
		}
	}

	private void addAnnotatedClassToConfiguration(Class<?> bean) {
		if (bean.getAnnotations() != null) {
			for (Annotation annotation : bean.getAnnotations()) {
				if (annotation instanceof javax.persistence.Table) {
					this.addAnnotatedClass(bean);
					break;
				}
			}
		}
	}
}
