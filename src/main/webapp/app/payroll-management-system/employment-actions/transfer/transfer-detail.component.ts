import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';

@Component({
  selector: 'jhi-employment-history-detail',
  templateUrl: './transfer-detail.component.html',
})
export class TransferDetailComponent implements OnInit {
  employmentHistory: IEmploymentHistory | null = null;
  employees: IEmployee[] = [];
  selectedEmployee: IEmployee | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentHistory }) => {
      this.employmentHistory = employmentHistory;
      this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => {
        this.employees = res.body || [];
        this.selectedEmployee = this.employees.filter(x => x.id === employmentHistory.employeeId)[0];
      });
    });
  }

  previousState(): void {
    window.history.back();
  }
}
