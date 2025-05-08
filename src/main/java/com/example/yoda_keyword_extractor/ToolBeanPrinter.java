package com.example.yoda_keyword_extractor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ToolBeanPrinter implements CommandLineRunner {
  private final ApplicationContext ctx;

  public ToolBeanPrinter(ApplicationContext ctx) {
    this.ctx = ctx;
  }

  @Override
  public void run(String... args) {
    System.out.println("=== Tool Beans ===");
    for (String beanName : ctx.getBeanDefinitionNames()) {
      Object bean = ctx.getBean(beanName);
      String className = bean.getClass().getName();
      if (className.contains("FileListerTool") || className.contains("KeywordExtractorTool")) {
        System.out.println(beanName + " : " + className);
      }
    }
    System.out.println("==================");
  }
}