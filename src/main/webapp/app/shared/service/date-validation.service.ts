import { Injectable } from '@angular/core';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

@Injectable({ providedIn: 'root' })
export class DateValidationService {
  // future date is not allowed for date of birth
  getDOBMaxDate(): NgbDateStruct {
    const current = new Date();
    const maxDate: NgbDateStruct = {
      year: current.getFullYear(),
      month: current.getMonth() + 1,
      day: current.getDate(),
    };
    return maxDate;
  }
}
