/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.ui.service;

import com.jarektoro.responsivelayout.ResponsiveColumn;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Artem
 */
@Theme("centralViewTheme")
@SpringUI
public class UiServiceMainUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setSizeFull();//Пользовательский интерфейс на весь экран
        //Базового макета
        BasicLayoutCreator main_layer = new BasicLayoutCreator();
        ResponsiveLayout main_layout = main_layer.main_layout;
        main_layout.setSizeFull();
        main_layout.setHeight("330%");
        setContent(main_layout);
        //Создание строки, для добавления конкретного контента на даную страницу
        ResponsiveRow slider_row = main_layer.content_row_layout.addRow();
        //Создание custom слоя для добавления слайдера
        CustomLayout slider_layout = new CustomLayout("SliderLayout");
        slider_row.addColumn().withDisplayRules(12, 12, 12, 12).withComponent(slider_layout);

        //Отрисовка заголовка топа рицептов
        ResponsiveRow recipe_title = main_layer.content_row_layout.addRow();
        CustomLayout top_recipe_title_layout = new CustomLayout("TopRecipeTitle");
        recipe_title.addColumn().withDisplayRules(12, 12, 12, 12).withComponent(top_recipe_title_layout);
        //Добавление кнопки в заголовок
        Button search_recipes_button = new Button("Найти нужный рецепт");

        ResponsiveRow rowForView = main_layer.content_row_layout.addRow();
        rowForView.addColumn().withDisplayRules(12, 12, 12,12).withComponent(new ShortViewOfReceipe());

        //JavaScript.getCurrent().execute("alert('Hello')");
        //ПРОСТО ДЛЯ ТЕСТА ОТКРЫТИЯ ОКНА
        search_recipes_button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                RegistrationForm modalWindow = new RegistrationForm();
                addWindow(modalWindow);
            }
        });

        top_recipe_title_layout.addComponent(search_recipes_button, "search_recipes_button");
        //Здесь можно разместить добавление рецептов либо фиксированно, например, топ 5, или
        //Задать количество по какому-либо другому параметру, например, по нажатию кнопки добавлять
        //еще несколько к имеющемуся списку
        for (int i = 0; i < 8; i++) {
            //Задание отступа между рецептами
            ResponsiveRow the_distance_between_recipe = main_layer.content_row_layout.addRow();
            the_distance_between_recipe.setHeight("30px");
            the_distance_between_recipe.addColumn().withDisplayRules(12, 12, 12, 12);
            //Отрисовка изображения рецепта
            ResponsiveRow recipe_row = main_layer.content_row_layout.addRow();
            Image top_image = new Image();
            top_image.setSource(new FileResource(new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/images/top1.png")));
            top_image.setHeight("70%");
            top_image.setWidth("100%");
            recipe_row.addColumn().withDisplayRules(2, 2, 2, 2).withComponent(top_image);
            //Создание custom макета с поддержкой flexbox
            CustomLayout top_recipe_layout = new CustomLayout("TopRecipeLayout");
            recipe_row.addColumn().withDisplayRules(8, 8, 8, 8).withComponent(top_recipe_layout);

            //Задание информации по каждому из рецептов
            Label recipes_name = new Label("Название");
            top_recipe_layout.addComponent(recipes_name, "recipes_name");
            Label recipes_author = new Label("Автор");
            top_recipe_layout.addComponent(recipes_author, "recipes_author");
            Button recepies_parts_button = new Button("Ингридиенты");
            top_recipe_layout.addComponent(recepies_parts_button, "parts_recipe_button");
            Label number_of_servings_lable = new Label(String.valueOf(i));//просто для примера
            top_recipe_layout.addComponent(number_of_servings_lable, "number_of_servings_lable");
            Label working_times_lable = new Label(String.valueOf(i));//просто для примера
            top_recipe_layout.addComponent(working_times_lable, "working_times_lable");
            Button add_recipe_to_favorites_button = new Button("Добавить в избранное");
            top_recipe_layout.addComponent(add_recipe_to_favorites_button, "add_recipe_to_favorites_button");
        }

        //Задание отступа до коцна страницы
        ResponsiveRow the_distance_between_bottom_and_recipes = main_layer.content_row_layout.addRow();
        the_distance_between_bottom_and_recipes.setHeight("60px");
        the_distance_between_bottom_and_recipes.addColumn().withDisplayRules(12, 12, 12, 12);
    }
}
