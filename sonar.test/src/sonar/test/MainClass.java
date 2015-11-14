package sonar.test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import sonar.core.Component;
import sonar.core.PropertyInfo;
import sonar.core.annotations.Model;
import sonar.core.annotations.Property;

public class MainClass {

	public static void main(String[] args) {
		Component comp = new Component();
		
		Model m = SampleModel.class.getAnnotation(Model.class);
		comp.setName(m.view());
		Field[] fi = SampleModel.class.getDeclaredFields();
		for (Field f : fi) {
			System.out.println(f.getName());
			Property an = f.getAnnotation(Property.class);
			if (an == null)
				continue;
			PropertyInfo pi = new PropertyInfo(an);
			if (pi.getName().length() == 0)
				pi.setName(f.getName());
			comp.Properties.add(pi);
		}
		//MainClass.class.getClassLoader().get
		InputStream is = MainClass.class.getClassLoader().getResourceAsStream(m.path());
		Scanner sc = new Scanner(is,"UTF-8");
		sc.useDelimiter("\\A");
		String str = sc.next();
		sc.close();
		comp.setContent(str);
	}

}
