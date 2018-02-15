import { StudentsPage } from './app.po';
import { browser, element, by } from 'protractor';

// slow down tests for better visualization
new StudentsPage().protractorSlowDownMs(500);
const dbTypeMongo = "mongo";
const dbTypeMysql = "mysql";

describe('Students App', () => {
  let page: StudentsPage;
  let testId;

  // beforeAll(() => {
  //   page = new StudentsPage();
  //   page.getMongoStudents().then(mongoStudents => {
  //     if (mongoStudents.length === 0) {
  //       page.addTestStudentToMongo(0);
  //     }
  //     page.getMysqlStudents().then(mysqlStudents => {
  //       if (mysqlStudents.length === 0) {
  //         page.addTestStudentToMysql(0);
  //       }
  //     })
  //   })
  // });

  beforeEach(() => {
    page = new StudentsPage();
  });

  it('should have a title set to Students Db', () => {
    page.navigateTo();
    expect(page.getTitle()).toBe('Students Db');
  });

  it('should add new student when form filled and "add student" clicked with "MongoDb" chosen', () => {
    page.navigateTo();
    page.getMongoStudents().then(students => {
      page.getAddStudentNavLink().click();

      page.getFormIdField().sendKeys(students.length);
      page.getFormNameField().sendKeys('Test');
      page.getFormAgeField().sendKeys(12);
      page.getFormGradeField().sendKeys(6);
      element(by.cssContainingText('option', 'MongoDb')).click();
      page.getStudentFormSubmit().click();

      browser.getCurrentUrl().then(url => {
        if (!url.startsWith('http://localhost:4200/add-student')) {
          // add was successfull
          expect(browser.getCurrentUrl()).toEqual('http://localhost:4200/students');
        } else {
          // add was unsuccessfull - toaster message should be displayed
          expect(page.getElementByCss('.ng-star-inserted').isPresent()).toBeTruthy
        }
      });
    })
  });

  it('should add new student when form filled and "add student" clicked with "MySql" chosen', () => {
    page.navigateTo();
    page.getMysqlStudents().then(students => {
      page.getAddStudentNavLink().click();

      page.getFormIdField().sendKeys(students.length);
      page.getFormNameField().sendKeys('Test');
      page.getFormAgeField().sendKeys(12);
      page.getFormGradeField().sendKeys(6);
      element(by.cssContainingText('option', 'MySql')).click();
      page.getStudentFormSubmit().click();

      browser.getCurrentUrl().then(url => {
        if (!url.startsWith('http://localhost:4200/add-student')) {
          // add was successfull
          expect(browser.getCurrentUrl()).toEqual('http://localhost:4200/students');
        } else {
          // add was unsuccessfull - toaster message should be displayed
          expect(page.getElementByCss('.ng-star-inserted').isPresent()).toBeTruthy
        }
      });
    })
  });

  it('should load students when loading page', () => {
    page.navigateTo();
    page.getElementByCss('.students ')
    page.getStudents().then(students => {
      expect(students.length).toBeGreaterThan(0);
      expect(students[0].getText()).toContain('Delete');
    })
  });

  it('should load student details page when "details" clicked', () => {
    page.navigateTo();
    page.getDetailsButton().click();

    let containsMongo = browser.getCurrentUrl().then(url => { return url.startsWith('http://localhost:4200/students/mongo/') });
    let containsMysql = browser.getCurrentUrl().then(url => { return url.startsWith('http://localhost:4200/students/mysql/') });
    let containsDetails = browser.getCurrentUrl().then(url => { return url.endsWith('/details') });
    expect(containsMongo || containsMysql).toBe(true);
    expect(containsDetails).toBe(true);
    expect(page.getElementByCss('.profile-container').isPresent()).toBeTruthy();
  });

  it('should open confirm dialog when "delete" clicked', () => {
    page.navigateTo();
    page.getDeleteButton(dbTypeMongo).click();

    // Remove the 'fade' class from the Bootstrap modal
    browser.executeScript("$('.modal').removeClass('fade');");
    expect(element(by.className('modal')).isDisplayed()).toBe(true);
  });

  it('should toggle mongo students when "MongoDb" is clicked', () => {
    page.navigateTo();
    let mongoDbDisplayed = page.getMongoStudentsList().isDisplayed();

    page.getMongoDbChangeButton().click();
    // expect if mongo was displayed before to not be present now and the opposite
    expect(!mongoDbDisplayed).toEqual(page.getMongoStudentsList().isPresent());
  });

  it('should toggle mysql students when "MySql" is clicked', () => {
    page.navigateTo();
    let mysqlDbDisplayed = page.getMySqlStudentsList().isDisplayed();

    page.getMySqlDbChangeButton().click();
    // expect if mongo was displayed before to not be present now and the opposite
    expect(!mysqlDbDisplayed).toEqual(page.getMySqlStudentsList().isPresent());
  });

  it('should load add-student page when "Add Student" clicked', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    let containsAddStudent = browser.getCurrentUrl().then(url => { return url.startsWith('http://localhost:4200/add-student') });
    expect(containsAddStudent).toBe(true);
    expect(page.getElementByCss('#add-student-form').isPresent()).toBeTruthy();
  });

  it('should load students(home) page when "Students Db" clicked in the navbar', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    //let containsAddStudent = browser.getCurrentUrl().then(url => { return url.startsWith('http://localhost:4200/add-student') });
    expect(browser.getCurrentUrl()).toBe('http://localhost:4200/add-student');
    page.getStudentsDbNavLink().click();
    expect(browser.getCurrentUrl()).toBe('http://localhost:4200/students');
  });

  it('should load all "Add Student" input fields', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    expect(page.getFormIdField().isPresent()).toBeTruthy();
    expect(page.getFormNameField().isPresent()).toBeTruthy();
    expect(page.getFormAgeField().isPresent()).toBeTruthy();
    expect(page.getFormGradeField().isPresent()).toBeTruthy();
    expect(page.getFormDbTypeField().isPresent()).toBeTruthy();
  });

  it('should DISPLAY warning if input field [id] visited and not filled', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    // warning is not present before visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(false);

    page.getFormIdField().click();
    page.getFormNameField().click();

    // warning is showed after visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(true);
    expect(page.getElementByCss('.help-block').getText()).toBe("Please enter student's id!");
  });

  it('should DISPLAY warning if input field [name] visited and not filled', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    // warning is not present before visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(false);

    page.getFormNameField().click();
    page.getFormAgeField().click();

    // warning is showed after visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(true);
    expect(page.getElementByCss('.help-block').getText()).toBe("Please enter student's name!");
  });

  it('should DISPLAY warning if input field [age] visited and not filled', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    // warning is not present before visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(false);

    page.getFormAgeField().click();
    page.getFormGradeField().click();

    // warning is showed after visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(true);
    expect(page.getElementByCss('.help-block').getText()).toBe("Please enter student's age!");
  });

  it('should DISPLAY warning if input field [grade] visited and not filled', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    // warning is not present before visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(false);

    page.getFormGradeField().click();
    page.getFormAgeField().click();

    // warning is showed after visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(true);
    expect(page.getElementByCss('.help-block').getText()).toBe("Please enter student's grade!");
  });

  it('should DISPLAY warning if select field [dbType] visited and not selected', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    // warning is not present before visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(false);

    page.getFormDbTypeField().click();
    page.getFormAgeField().click();

    // warning is showed after visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(true);
    expect(page.getElementByCss('.help-block').getText()).toBe("Please select a database!");
  });

  it('should NOT display warning if input field visited and filled (valid)', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    // warning is not present before visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(false);

    page.getFormIdField().click();
    page.getFormIdField().sendKeys(5);

    page.getFormNameField().click();

    // warning is showed after visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(false);
  });

  it('should HIDE input warning if input field gets filled (made valid)', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();

    // warning is not present before visit of input field
    expect(page.getElementByCss('.help-block').isPresent()).toBe(false);

    page.getFormNameField().click();
    page.getFormAgeField().click();

    expect(page.getElementByCss('.help-block').isPresent()).toBe(true);

    page.getFormNameField().sendKeys('Test');
    page.getFormAgeField().sendKeys(10);

    expect(page.getElementByCss('.help-block').isPresent()).toBe(false);
  });

  it('should "Add student" button be disabled on page load', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();
    let submit = page.getStudentFormSubmit();
    expect(submit.isEnabled()).toBe(false);
  });

  it('should "Add student" button be disabled if form filled but not valid', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();
    let submit = page.getStudentFormSubmit();
    page.getFormIdField().sendKeys(0);
    page.getFormNameField().sendKeys("Test");
    page.getFormGradeField().sendKeys(5);
    element(by.cssContainingText('option', 'MySql')).click();

    // age field not set
    expect(submit.isEnabled()).toBe(false);
  });

  it('should "Add student" button be enabled if form filled and valid', () => {
    page.navigateTo();
    page.getAddStudentNavLink().click();
    let submit = page.getStudentFormSubmit();
    page.getFormIdField().sendKeys(0);
    page.getFormNameField().sendKeys("Test");
    page.getFormAgeField().sendKeys(12);
    page.getFormGradeField().sendKeys(5);
    element(by.cssContainingText('option', 'MySql')).click();

    expect(submit.isEnabled()).toBe(true);
  });

  it('should delete last student from MongoDb when "yes" clicked', () => {
    page.navigateTo();

    page.getStudents().then(students => {
      let count = students.length;
      page.getDeleteButton(dbTypeMongo).click();
      browser.executeScript("$('.modal').removeClass('fade');");

      // delete student
      page.getElementByCss('.modal .btn-default').click();

      page.getStudents().then(studentsAfterDelete => {
        if (studentsAfterDelete) {
          expect(count - 1 === studentsAfterDelete.length).toBe(true);
        } else {
          expect(count - 1 === 0).toBe(true);
        }
      });
    })
  });

  it('should delete last student from MySql when "yes" clicked', () => {
    page.navigateTo();

    page.getStudents().then(students => {
      let count = students.length;
      page.getDeleteButton(dbTypeMysql).click();
      browser.executeScript("$('.modal').removeClass('fade');");

      // delete student
      page.getElementByCss('.modal .btn-default').click();

      page.getStudents().then(studentsAfterDelete => {
        if (studentsAfterDelete) {
          expect(count - 1 === studentsAfterDelete.length).toBe(true);
        } else {
          expect(count - 1 === 0).toBe(true);
        }
      });
    })
  });
});

