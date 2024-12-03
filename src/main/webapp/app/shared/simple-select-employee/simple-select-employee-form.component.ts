import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { EmployeeCommonService } from '../../common/employee-address-book/employee-common.service';
import { EmployeeMinimalListType } from 'app/shared/model/enumerations/employee-minimal-list-type.model';
import { NgSelectComponent } from '@ng-select/ng-select';
import { IEmployee } from '../../entities/employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-simple-select-employee-form',
  templateUrl: './simple-select-employee-form.component.html',
  styleUrls: ['./select-employee-form.component.scss'],
})
export class SimpleSelectEmployeeFormComponent implements OnInit {
  @Input()
  selectedFieldValue!: number;

  @Input()
  selectFieldName = 'id';

  @Input()
  listType: EmployeeMinimalListType = EmployeeMinimalListType.ALL;

  @Input()
  showAllEmployeeSelection = false;

  @Input() isDisabled: boolean = false;

  @Output() onChangeEmployee = new EventEmitter<IEmployee>();

  selectedEmployee: IEmployee | null = null;

  employees: IEmployee[] = [];

  @ViewChild(NgSelectComponent) ngSelectComponent!: NgSelectComponent;

  constructor(private employeeService: EmployeeCommonService) {}

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

        if (this.showAllEmployeeSelection) {
          this.employees.unshift({
            id: undefined,
            fullName: 'Select All Employee',
          });
        }
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

        if (this.showAllEmployeeSelection) {
          this.employees.unshift({
            id: undefined,
            fullName: 'Select All Employee',
          });
        }
      });
    } else if (this.listType === EmployeeMinimalListType.MY_TEAM) {
      this.employeeService.getMyTeamMembers().subscribe((res: HttpResponse<IEmployee[]>) => {
        if (!this.selectedFieldValue) {
          this.selectedFieldValue = res.body![0].id!;
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

        if (this.showAllEmployeeSelection) {
          this.employees.unshift({
            id: undefined,
            fullName: 'Select All Employee',
          });
        }
      });
    }
  }

  onChange(value: any): void {
    this.onChangeEmployee.emit(value);
  }
}
