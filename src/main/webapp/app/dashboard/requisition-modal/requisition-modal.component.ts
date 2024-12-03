import { Component, OnInit } from '@angular/core';
import { DashboardModalsCommon } from '../dashboard-modals-common';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { SearchModalService } from '../../shared/specialized-search/search-modal/search-modal.service';
import Swal from 'sweetalert2';
import { HttpResponse } from '@angular/common/http';
import { IRrfRaiseValidity } from '../../shared/model/rrf-raise-validity.model';
import { SWAL_REJECTED_ICON } from '../../shared/swal-common/swal.properties.constant';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { RecruitmentRequisitionFormService } from '../../shared/legacy/legacy-service/recruitment-requisition-form.service';
import { ConfigService } from '../../entities/config/service/config.service';
import { DefinedKeys } from '../../config/defined-keys.constant';

@Component({
  selector: 'jhi-requisition-modal',
  templateUrl: './requisition-modal.component.html',
  styleUrls: ['../dashboard.scss'],
})
export class RequisitionModalComponent implements OnInit {
  commonService!: DashboardModalsCommon;
  isValidUserPfStatement!: boolean;
  currentEmployee!: IEmployee;
  canRaiseRRF = false;
  canRaiseRRFOnBehalf = false;
  isRrfEnabled = false;

  constructor(
    protected activeModal: NgbActiveModal,
    private router: Router,
    protected modalService: NgbModal,
    protected searchModalService: SearchModalService,
    protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    protected configService: ConfigService
  ) {
    this.commonService = new DashboardModalsCommon(this.activeModal, this.router);
  }
  ngOnInit(): void {
    this.checkRRFEligibility();
    this.checkRRFEnabled();
  }

  checkRRFEligibility(): void {
    this.canRaiseRRF = false;
    this.canRaiseRRFOnBehalf = false;

    this.recruitmentRequisitionFormService.canRaiseRRF().subscribe((res: HttpResponse<IRrfRaiseValidity>) => {
      if (res.body!.canRaiseRRFOnBehalf && res.body!.canRaiseRRFOwn) {
        this.canRaiseRRFOnBehalf = true;
        this.canRaiseRRF = true;
      } else if (res.body!.canRaiseRRFOwn) {
        this.canRaiseRRF = true;
      }
    });
  }

  navigateToRRFPage(routeLink: string): void {
    if (this.canRaiseRRF) {
      this.router.navigate([routeLink]);
    } else {
      Swal.fire({
        icon: SWAL_REJECTED_ICON,
        text: 'Oops! You cannot raise RRF',
        timer: 2500,
        showConfirmButton: false,
      });
    }
  }

  navigateToRRFOnBehalfPage(routeLink: string): void {
    if (this.canRaiseRRFOnBehalf) {
      this.router.navigate([routeLink]);
    } else {
      Swal.fire({
        icon: SWAL_REJECTED_ICON,
        text: 'Oops! You cannot raise RRF',
        timer: 2500,
        showConfirmButton: false,
      });
    }
  }

  checkRRFEnabled(): void {
    this.configService.findByKeyCommon(DefinedKeys.is_rrf_enabled_for_user_end).subscribe(res => {
      if (res.body!.value === 'FALSE') {
        this.isRrfEnabled = false;
      } else this.isRrfEnabled = true;
    });
  }
}
