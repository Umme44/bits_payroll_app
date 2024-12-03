import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';

import { swalOnDeleteSuccess, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import Swal from 'sweetalert2';
import { SWAL_CONFIRM_BTN_TEXT, SWAL_DELETE_CONFIRMATION, SWAL_DENY_BTN_TEXT } from 'app/shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from 'app/shared/constants/color.code.constant';
import { IEducationDetails } from '../../../education-details/education-details.model';
import { IEmployee } from '../../employee-custom.model';
import { EducationDetailsService } from '../../../education-details/service/education-details.service';
import { EmployeeCustomService } from '../../service/employee-custom.service';

@Component({
  selector: 'jhi-employee-education-details',
  templateUrl: './education-details.modal.component.html',
})
export class EducationDetailsModalComponent implements OnInit {
  educationDetails?: IEducationDetails[];
  eventSubscriber?: Subscription;
  employees: IEmployee[] = [];
  employee!: IEmployee;
  employeeId!: number;
  educationDetailsIdForUpdate = -1;

  constructor(
    protected educationDetailsService: EducationDetailsService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute,
    protected employeeService: EmployeeCustomService,
    protected activeModal: NgbActiveModal
  ) {}

  loadAll(): void {
    this.educationDetailsService
      .queryByEmployeeId(this.employeeId)
      .subscribe((res: HttpResponse<IEducationDetails[]>) => (this.educationDetails = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEducationDetails): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  delete(educationDetails: IEducationDetails): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.educationDetailsService.delete(educationDetails.id!).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.loadAll();
          },
          () => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  dismiss(): void {
    this.activeModal.dismiss('Cross click');
  }

  populateEditForm(id: number): void {
    this.educationDetailsIdForUpdate = id;
  }
}
