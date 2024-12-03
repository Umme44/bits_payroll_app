import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

import Swal from 'sweetalert2';
import { EmployeeCommonService } from 'app/common/employee-address-book/employee-common.service';
import { ISalaryCertificate } from '../salary-certificate.model';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import { SalaryCertificateService } from '../service/salary-certificate.service';
import {
  SWAL_APPROVE_CONFIRMATION,
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT, SWAL_REJECT_CONFIRMATION
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';
import {
  swalOnApprovedSuccess,
  swalOnRejectedSuccess,
  swalOnRequestError
} from '../../../shared/swal-common/swal-common';
import { IEmployee } from '../../employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-salary-certificate-approval',
  templateUrl: './salary-certificate-approval.component.html',
})
export class SalaryCertificateApprovalComponent implements OnInit {
  salaryCertificateApplications: ISalaryCertificate[] = [];
  allSelector = false;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  searchText = new FormControl('');
  // signatoryPersonId!: number;
  listOfSignatoryPersons: IEmployee[] = [];

  salaryCertificateApprovalForm = this.fb.group({
    signatoryPersonId: [null, Validators.required],
  });

  constructor(
    protected salaryCertificateService: SalaryCertificateService,
    private employeeService: EmployeeCommonService,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.salaryCertificateService.getAllPending().subscribe(res => {
      this.salaryCertificateApplications = res.body!;
    });
    this.approvalDTO.listOfIds = [];

    this.loadAllEmployees();
  }

  loadAllEmployees(): void {
    this.employeeService.getAllMinimal().subscribe(res => {
      this.listOfSignatoryPersons = res.body!;
      this.listOfSignatoryPersons = this.listOfSignatoryPersons.map(item => {
        return {
          id: item.id,
          pin: item.pin,
          name: item.fullName,
          designation: item.designationName,
          fullName: item.pin + ' - ' + item.fullName + ' - ' + item.designationName,
        };
      });
    });
  }

  onChange($event: any): void {
    const id = Number($event.target.value);
    const isChecked = $event.target.checked;
    if (this.salaryCertificateApplications !== undefined) {
      this.salaryCertificateApplications = this.salaryCertificateApplications.map(d => {
        if (d.id === id) {
          d.isChecked = isChecked;
          this.allSelector = false;
          return d;
        }
        if (id === -1) {
          d.isChecked = this.allSelector;
          return d;
        }
        return d;
      });
    }
    // clear previous set
    this.selectedIdSet.clear();
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    for (let i = 0; i < this.salaryCertificateApplications!.length; i++) {
      // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
      if (this.salaryCertificateApplications![i].isChecked === true) {
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
        this.selectedIdSet.add(this.salaryCertificateApplications![i].id);
      }
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => value as number);
  }

  search(): void {}

  loadAll(): void {
    this.salaryCertificateService.getAllPending().subscribe((res: HttpResponse<ISalaryCertificate[]>) => {
      if (res.body) {
        this.salaryCertificateApplications = res.body;
      } else this.salaryCertificateApplications = [];
    });
  }

  approveSelected(content: any): void {
    this.modalService.open(content).result.then(
      result => {
        Swal.fire({
          text: SWAL_APPROVE_CONFIRMATION,
          showCancelButton: true,
          confirmButtonColor: PRIMARY_COLOR,
          cancelButtonColor: DANGER_COLOR,
          confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
          cancelButtonText: SWAL_CANCEL_BTN_TEXT,
        }).then(res => {
          if (res.isConfirmed) {
            const signatoryPersonId = this.salaryCertificateApprovalForm.get(['signatoryPersonId'])!.value;
            this.salaryCertificateService
              .approveSelected(this.approvalDTO, signatoryPersonId)
              .subscribe((response: HttpResponse<boolean>) => {
                if (response.body === true) {
                  swalOnApprovedSuccess();
                  this.clearAllChecks();
                  this.loadAll();
                } else {
                  swalOnRequestError();
                  this.salaryCertificateApprovalForm.get(['signatoryPersonId'])!.reset();
                }
              });
          }
        });
      },
      reason => {
        this.salaryCertificateApprovalForm.get(['signatoryPersonId'])!.reset();
      }
    );
  }

  rejectSelected(): void {
    Swal.fire({
      text: SWAL_REJECT_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.salaryCertificateService.rejectSelected(this.approvalDTO).subscribe((res: HttpResponse<boolean>) => {
          if (res.body === true) {
            swalOnRejectedSuccess();
            this.clearAllChecks();
            this.loadAll();
          } else {
            swalOnRequestError();
          }
        });
      }
    });
  }

  clearAllChecks(): void {
    this.allSelector = false;
    this.salaryCertificateApplications?.map(salaryCertificate => {
      salaryCertificate.isChecked = false;
    });
    this.salaryCertificateApplications = [];
    this.approvalDTO.listOfIds = [];
  }

  monthNameNormaCapitalize(month: any): string {
    month = month.toString().toLowerCase();
    month = month.charAt(0).toUpperCase() + month.slice(1).toLowerCase();
    return month;
  }
}
