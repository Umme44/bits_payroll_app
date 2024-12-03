import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { EmployeeCommonService } from '../../common/employee-address-book/employee-common.service';
import { EmployeeMinimalListType } from 'app/shared/model/enumerations/employee-minimal-list-type.model';
import { IEmployee } from '../../entities/employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-select-employee-form',
  templateUrl: './select-employee-form.component.html',
  styleUrls: ['./select-employee-form.component.scss'],
})
export class SelectEmployeeFormComponent implements OnInit {
  @Input()
  employeeIdForm!: FormGroup;

  @Input()
  selectedId!: number;

  @Input()
  listType: EmployeeMinimalListType = EmployeeMinimalListType.ALL;

  @Output() onChangeEmployeeId = new EventEmitter<number>();

  selectedEmployee: IEmployee | null = null;

  employees: IEmployee[] = [];

  constructor(private employeeService: EmployeeCommonService, private fb: FormBuilder) {}

  ngOnInit(): void {
    if (this.listType === EmployeeMinimalListType.ACTIVE) {
      this.employeeService.getAllMinimalActive().subscribe((res: HttpResponse<IEmployee[]>) => {
        this.employees = res.body || [];

        this.employees = this.employees.map(item => {
          return {
            id: item.id,
            pin: item.pin,
            name: item.fullName,
            designation: item.designationName,
            fullName: item.pin + ' - ' + item.fullName + ' - ' + item.designationName,
          };
        });
      });
    } else if (this.listType === EmployeeMinimalListType.ALL) {
      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => {
        this.employees = res.body || [];
        this.employees = this.employees.map(item => {
          return {
            id: item.id,
            pin: item.pin,
            name: item.fullName,
            designation: item.designationName,
            fullName: item.pin + ' - ' + item.fullName + ' - ' + item.designationName,
          };
        });
      });
    } else if (this.listType === EmployeeMinimalListType.MY_TEAM) {
      this.employeeService.getMyTeamMembers().subscribe((res: HttpResponse<IEmployee[]>) => {
        if (!this.selectedId) {
          this.selectedId = res.body![0].id!;
        }
        this.employees = res.body || [];
        this.employees = this.employees.map(item => {
          return {
            id: item.id,
            pin: item.pin,
            name: item.fullName,
            designation: item.designationName,
            fullName: item.pin + ' - ' + item.fullName + ' - ' + item.designationName,
          };
        });
        if (!this.selectedId) {
          this.selectedId = this.employees[0].id!;
        }
      });
    }
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  setEmployee(id: number): void {
    this.selectedId = id;
    for (const employee of this.employees) {
      if (employee.id === id) {
        this.selectedEmployee = employee;
        return;
      }
    }
  }

  onChangeSelectEmployee(selectedEmployee: any): void {
    if (selectedEmployee) {
      this.onChangeEmployeeId.emit(selectedEmployee.id);
    } else {
      this.onChangeEmployeeId.emit(undefined);
    }
  }
}
