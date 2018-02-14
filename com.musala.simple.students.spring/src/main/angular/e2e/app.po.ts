import { browser, by, element, Key, Button, protractor } from 'protractor';

export class StudentsPage {
  navigateTo() {
    browser.ignoreSynchronization = true;
    browser.driver.get('localhost:4200/');
  }

  addTestStudentToMysql(id) {
    this.fillStudentForm(id, "Test", 10, 3);
      element(by.cssContainingText('option', 'MySql')).click();  
      this.getStudentFormSubmit().click();  
  }

  addTestStudentToMongo(id) {
    this.fillStudentForm(id, "Test", 10, 3);
      element(by.cssContainingText('option', 'MongoDb')).click();  
      this.getStudentFormSubmit().click();  
  }

  fillStudentForm(id, name, age, grade) {
    this.getFormIdField().sendKeys(id);
    this.getFormNameField().sendKeys(name);
    this.getFormAgeField().sendKeys(age);
    this.getFormGradeField().sendKeys(grade);
  }

  wait() {
    browser.waitForAngular();
  }

  getTitle() {
    return browser.getTitle();
  }

  getStudents() {
    return element.all(by.css('.students tr'));
  }

  getMongoStudents() {
    return element.all(by.css('#mongo-tbody tr'));
  }

  getMysqlStudents() {
    return element.all(by.css('#mysql-tbody tr'));
  }

  getElementByCss(css) {
    return element(by.css(css));
  }

  getDetailsButton() {
    return element(by.css('.details'));
  }

  getDeleteButton(dbType) {
    switch(dbType) {
      case "mongo": return element.all(by.css('#mongo-tbody tr .delete')).last();
      case "mysql": return element.all(by.css('#mysql-tbody tr .delete')).last();
    }
  }

  getMongoStudentsList() {
    return element(by.css('#mongo-students-list'));
  }

  getMySqlStudentsList() {
    return element(by.css('#mysql-students-list'));
  }

  getStudentsDbNavLink() {
    return element(by.css('#students-home-link'));
  }

  getAddStudentNavLink() {
    return element(by.css('#add-student-link'));
  }

  getFormIdField() {
    return element(by.css('#id'));
  }

  getFormNameField() {
    return element(by.css('#name'));
  }

  getFormAgeField() {
    return element(by.css('#age'));
  }

  getFormGradeField() {
    return element(by.css('#grade'));
  }

  getFormDbTypeField() {
    return element(by.css('#dbType'));
  }

  getMySqlDbChangeButton() {
    return element(by.css('#db-toggle-mysql'));
  }

  getMongoDbChangeButton() {
    return element(by.css('#db-toggle-mongo'));
  }

  getStudentFormSubmit() {
    return element(by.css('#add-student-submit'));
  }

  hasClass(element, className) {
    return element.getAttribute('class').then((classes) => {
      return classes.split(' ').indexOf(className) !== -1;
    })
  }

  protractorSlowDownMs(milliseconds) {
    var origFn = browser.driver.controlFlow().execute;

    browser.driver.controlFlow().execute = function () {
      var args = arguments;

      // queue 100ms wait
      origFn.call(browser.driver.controlFlow(), function () {
        return protractor.promise.delayed(milliseconds);
      });

      return origFn.apply(browser.driver.controlFlow(), args);
    };
  }
}