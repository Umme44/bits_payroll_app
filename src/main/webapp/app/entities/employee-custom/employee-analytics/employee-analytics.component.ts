import { Component, OnInit } from '@angular/core';
import { IEmployeeDashboardAnalytics } from '../../../shared/model/employee-dashboard-analytics.model';
import { EmployeeCustomService } from '../service/employee-custom.service';

@Component({
  selector: 'jhi-employee-analytics',
  templateUrl: './employee-analytics.component.html',
  styleUrls: ['employee-analytics.component.scss'],
})
export class EmployeeAnalyticsComponent implements OnInit {
  employeeDashboardAnalytics!: IEmployeeDashboardAnalytics;

  constructor(protected employeeService: EmployeeCustomService) {}

  ngOnInit(): void {
    this.employeeService.getEmployeeDashboardAnalytics().subscribe(res => {
      this.employeeDashboardAnalytics = res.body!;
    });
  }
}
