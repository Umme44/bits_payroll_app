import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { swalOnDeleteSuccess, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import Swal from 'sweetalert2';
import { SWAL_CONFIRM_BTN_TEXT, SWAL_DELETE_CONFIRMATION, SWAL_DENY_BTN_TEXT } from 'app/shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from 'app/shared/constants/color.code.constant';
import { IWorkingExperience } from '../../../working-experience/working-experience.model';
import { IEmployee } from '../../employee-custom.model';
import { WorkingExperienceService } from '../../../working-experience/service/working-experience.service';
import { EmployeeCustomService } from '../../service/employee-custom.service';

@Component({
  selector: 'jhi-employee-work-experience-modal',
  templateUrl: './employee-work-experience.modal.component.html',
})
export class EmployeeWorkExperienceModalComponent implements OnInit {
  workingExperiences?: IWorkingExperience[];
  eventSubscriber?: Subscription;
  employeeId!: number;
  employee!: IEmployee;
  idForUpdate = -1;

  constructor(
    protected workingExperienceService: WorkingExperienceService,
    protected modalService: NgbModal,
    protected employeeService: EmployeeCustomService,
    protected activeModal: NgbActiveModal
  ) {}

  loadAll(): void {
    this.workingExperienceService
      .queryByEmployeeId(this.employeeId)
      .subscribe((res: HttpResponse<IWorkingExperience[]>) => (this.workingExperiences = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWorkingExperience): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  delete(workingExperience: IWorkingExperience): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.workingExperienceService.delete(workingExperience.id!).subscribe(
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
    this.idForUpdate = id;
  }
}
