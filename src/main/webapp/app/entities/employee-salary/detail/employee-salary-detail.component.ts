import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IAllowanceName } from 'app/shared/model/allowance-name.model';
import {IEmployeeSalary} from "../employee-salary.model";
import {ConfigService} from "../../config/service/config.service";

@Component({
  selector: 'jhi-employee-salary-detail',
  templateUrl: './employee-salary-detail.component.html',
})
export class EmployeeSalaryDetailComponent implements OnInit {
  employeeSalary: IEmployeeSalary | null = null;
  allowanceName: IAllowanceName | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected configService: ConfigService) {}

  ngOnInit(): void {
    this.configService.getAllowanceName().subscribe(res => {
      this.allowanceName = res.body;
    });
    this.activatedRoute.data.subscribe(({ employeeSalary }) => (this.employeeSalary = employeeSalary));
  }

  previousState(): void {
    window.history.back();
  }
}
