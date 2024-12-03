import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEmployeePin } from '../employee-pin.model';
import { EmployeeCategory } from '../../enumerations/employee-category.model';

@Component({
  selector: 'jhi-employee-pin-detail',
  templateUrl: './employee-pin-detail.component.html',
})
export class EmployeePinDetailComponent implements OnInit {
  employeePin!: IEmployeePin;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeePin }) => (this.employeePin = employeePin));
  }

  previousState(): void {
    window.history.back();
  }

  getEmployeeCategory(category: string): string {
    if (category === EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
      return 'By Contract';
    } else if (category === EmployeeCategory.INTERN) {
      return 'Intern';
    } else {
      return 'Regular';
    }
  }
}
