import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

import { Status } from 'app/shared/model/enumerations/status.model';
import { SimpleSelectEmployeeFormComponent } from 'app/shared/simple-select-employee/simple-select-employee-form.component';
import { EmployeeCommonService } from 'app/common/employee-address-book/employee-common.service';
import { EmployeeCategory } from 'app/shared/model/enumerations/employee-category.model';
import { INominee } from '../nominee.model';
import { IEmployee } from '../../employee-custom/employee-custom.model';
import { NomineeService } from '../service/nominee.service';
import { DATE_FORMAT } from '../../../config/input.constants';
import { IEmployeeNomineeInfo } from '../employee-nominee-info.model';

@Component({
  selector: 'jhi-nominee',
  templateUrl: './nominee.component.html',
})
export class NomineeComponent implements OnInit {
  nominees: INominee[];
  employeesNomineeInfo: IEmployeeNomineeInfo[] = [];
  employees: IEmployee[] = [];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  selectedEmployeeId!: number;
  status!: Status;
  selectedStatus = 'ALL';
  nomineeType!: string;
  isInvalid!: boolean;

  editForm = this.fb.group({
    startDate: [null, Validators.required],
    endDate: [null, [Validators.required]],
    nomineeType: [''],
  });

  employeeId = new FormControl('');

  @ViewChild('simpleSelectEmployeeFormComponent') simpleSelectEmployeeFormComponent!: SimpleSelectEmployeeFormComponent;
  constructor(
    protected nomineeService: NomineeService,
    protected modalService: NgbModal,
    private fb: FormBuilder,
    private employeeService: EmployeeCommonService
  ) {
    this.nominees = [];
    this.itemsPerPage = 20;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.isInvalid = false;
    this.nomineeType = 'all';
  }

  loadAll(): void {
    this.nomineeService
      .getEmployeesWithNominees({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        nomineeType: this.nomineeType,
      })
      .subscribe(res => (this.employeesNomineeInfo = res.body || []));
  }

  reset(): void {
    this.page = 0;
    this.nominees = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.employeeService.getAllMinimal().subscribe(res => {
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
    this.loadAll();
  }

  trackId(index: number, item: INominee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  // protected paginateNominees(data: IEmployeeNomineeInfo[] | null, headers: HttpHeaders): void {
  //   const headersLink = headers.get('link');
  //   this.links = this.parseLinks.parse(headersLink ? headersLink : '');
  //   if (data) {
  //     for (let i = 0; i < data.length; i++) {
  //       this.employeesNomineeInfo.push(data[i]);
  //     }
  //   }
  // }

  getUIFriendly(employeeCategory: EmployeeCategory): string {
    if (employeeCategory === EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) return 'Regular Confirmed';
    if (employeeCategory === EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) return 'Regular Probation';
    if (employeeCategory === EmployeeCategory.CONTRACTUAL_EMPLOYEE) return 'Contractual';
    if (employeeCategory === EmployeeCategory.INTERN) return 'Intern';

    return 'Part Time';
  }

  onSearchTextChange(employee: Event): void {
    const employeeId = Number.parseInt(this.employeeId.value, 10);
    if (employeeId === null || employeeId === undefined) {
      this.employeesNomineeInfo = [];
      this.reset();
    } else {
      this.selectedEmployeeId = employeeId;
      this.nomineeService
        .getEmployeesWithNominees({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
          employeeId: this.selectedEmployeeId,
          startDate:
            this.editForm.get(['startDate'])!.value === undefined || this.editForm.get(['startDate'])!.value === null
              ? undefined
              : this.editForm.get(['startDate'])!.value.format(DATE_FORMAT),
          endDate:
            this.editForm.get(['endDate'])!.value === undefined || this.editForm.get(['endDate'])!.value === null
              ? undefined
              : this.editForm.get(['endDate'])!.value.format(DATE_FORMAT),
          nomineeType: this.nomineeType,
        })
        .subscribe((res: HttpResponse<IEmployeeNomineeInfo[]>) => {
          this.employeesNomineeInfo = res.body!;
        });
    }
  }

  onKeydown(event: any): void {
    if (event.key === 'Backspace') {
      this.employeesNomineeInfo = [];
      this.loadAll();
    }
  }

  filterByApprovalType(event: any): void {}

  addressSlice(address: string): String {
    return address.slice(0, 25) + '...';
  }

  closeModal(): void {
    this.modalService.dismissAll();
    this.editForm.reset();
    this.nomineeType = 'all';
  }

  open(content: any): void {
    this.nomineeType = 'General';
    this.editForm.get(['nomineeType'])!.setValue('General');
    const modalRef = this.modalService.open(content, { size: 'lg', backdrop: 'static', keyboard: false });
  }

  checkTime(event: any): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;

    if (startDate === undefined || endDate === undefined) {
      this.isInvalid;
    }

    if (startDate && endDate && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  searchByDate(): void {
    /*this.simpleSelectEmployeeFormComponent.ngSelectComponent.clearModel();*/

    this.employeeId.setValue(null);
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;
    this.nomineeType = this.editForm.get(['nomineeType'])!.value;
    if (
      this.nomineeType !== null &&
      this.nomineeType !== undefined &&
      startDate !== undefined &&
      startDate !== null &&
      endDate !== undefined &&
      endDate !== null
    ) {
      this.employeesNomineeInfo = [];
      this.nomineeService
        .getEmployeesWithNominees({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
          startDate: this.editForm.get(['startDate'])!.value.format(DATE_FORMAT),
          endDate: this.editForm.get(['endDate'])!.value.format(DATE_FORMAT),
          nomineeType: this.editForm.get(['nomineeType'])!.value,
        })
        .subscribe((res: HttpResponse<IEmployeeNomineeInfo[]>) => (this.employeesNomineeInfo = res.body!));
    }
    this.editForm.reset();
    this.modalService.dismissAll();
  }

  resetList(): void {
    /* this.simpleSelectEmployeeFormComponent.ngSelectComponent.clearModel();*/
    this.employeeId.setValue(null);
    this.nomineeType = 'all';
    this.nomineeService
      .getEmployeesWithNominees({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(res => {
        this.employeesNomineeInfo = [];
        this.employeesNomineeInfo = res.body!;
      });
    this.editForm.reset();
  }

  nomineeExportDownload(): void {
    const fileName = 'Nominee.xlsx';
    this.nomineeService.nomineeExportDownload().subscribe(x => {
      // It is necessary to create a new blob object with mime-type explicitly set
      // otherwise only Chrome works like it should
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

      // IE doesn't allow using a blob object directly as link href
      // instead it is necessary to use msSaveOrOpenBlob
      if ((window.navigator as any) && (window.navigator as any).msSaveOrOpenBlob) {
        (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
        return;
      }

      // For other browsers:
      // Create a link pointing to the ObjectURL containing the blob.
      const data = window.URL.createObjectURL(newBlob);

      const link = document.createElement('a');
      link.href = data;
      link.download = fileName;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      // tslint:disable-next-line:typedef
      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
  }
}
