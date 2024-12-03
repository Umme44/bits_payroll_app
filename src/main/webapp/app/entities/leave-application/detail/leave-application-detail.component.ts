import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import {ILeaveApplication} from "../leave-application.model";
import {IEmployee} from "../../employee/employee.model";
import {DataUtils} from "../../../core/util/data-util.service";
import {EmployeeService} from "../../employee/service/employee.service";

@Component({
  selector: 'jhi-leave-application-detail',
  templateUrl: './leave-application-detail.component.html'
})
export class LeaveApplicationDetailComponent implements OnInit {
  leaveApplication: ILeaveApplication | null = null;
  employee: IEmployee | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute, protected employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveApplication }) => (this.leaveApplication = leaveApplication));
    if (this.leaveApplication!.employeeId != null) {
      this.employeeService
        .find(this.leaveApplication!.employeeId)
        .subscribe((res: HttpResponse<IEmployee>) => (this.employee = res.body || null));
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
