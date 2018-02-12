import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Rx';
import { Toast } from 'angular2-toaster';


class CustomToast {
  generateToast(info, title, message) {
    var toast: Toast = {
      type: info,
      title: title,
      body: message
    };
    return toast;
  }
}

export { CustomToast };


