import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { INominee } from '../nominee.model';
import { IEmployee } from '../../employee-custom/employee-custom.model';
import { INomineeMaster, NomineeMaster } from '../../../shared/model/nominee-master.model';
import { IPfNominee } from '../pf-nominee.model';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import { NomineeService } from '../service/nominee.service';
import { PfNomineeService } from '../service/pf-nominee.service';
import { GeneralNomineeDetailsModalComponent } from '../modals/general-nominee-details-modal/general-nominee-details.modal.component';
import { PfNomineeDetailsModalComponent } from '../modals/pf-nominee-details-modal/pf-nominee-details.modal.component';
import {
  swalChangesNotSaved,
  swalForErrorWithMessage,
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRejectionForNomineeReport,
  swalOnRequestError,
  swalSuccessWithMessage,
} from '../../../shared/swal-common/swal-common';
import { swalConfirmationWithMessage } from '../../../shared/swal-common/swal-confirmation.common';
import { PfNomineeApprovalService } from '../service/pf-nominee-approval-service';
import { NomineeType } from '../../../shared/model/enumerations/nominee-type.model';
import { GeneralGfNomineeApprovalService } from '../service/general-gf-nominee-approval-service';

@Component({
  selector: 'jhi-nominee-summary',
  templateUrl: './nominee-summary-admin.component.html',
  styleUrls: ['./nominee-summary-admin.component.scss'],
})
export class NomineeSummaryAdminComponent implements OnInit, OnDestroy {
  nominee!: INominee;
  employee!: IEmployee;
  nominees!: INominee[];
  nomineesMaster!: INomineeMaster;
  gfNominees: INominee[] = [];
  pfNominees: IPfNominee[] = [];
  generalNominees: INominee[] = [];
  employeePin!: string;
  totalConsumedPercentage = 0;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  isGeneralApproving = false;
  isGFApproving = false;
  isPFApproving = false;
  isApproving = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected nomineeService: NomineeService,
    protected pfNomineeService: PfNomineeService,
    private modalService: NgbModal,
    protected router: Router,
    protected generalGfNomineeApprovalService: GeneralGfNomineeApprovalService,
    protected pfNomineeApprovalService: PfNomineeApprovalService
  ) {}

  ngOnInit(): void {
    this.employeePin = this.activatedRoute.snapshot.params['pin'];
    this.loadEmployeeDetails(this.employeePin);
  }

  loadEmployeeDetails(employeePin: string): void {
    this.nomineeService.getNomineesByEmployeePin(employeePin).subscribe((res: INomineeMaster) => {
      this.nomineesMaster = res;
      this.generalNominees = this.nomineesMaster.nomineeList!.filter(s => s.nomineeType === 'GENERAL');
      this.gfNominees = this.nomineesMaster.nomineeList!.filter(s => s.nomineeType === 'GRATUITY_FUND');
      this.pfNominees = this.nomineesMaster.pfNomineeDTOList!;
    });
  }

  ngOnDestroy(): void {
    this.modalService.dismissAll();
  }

  openNomineeDetails(nominee: INominee): void {
    const modalRef = this.modalService.open(GeneralNomineeDetailsModalComponent, { size: 'lg', backdrop: true, keyboard: false });
    modalRef.componentInstance.nominee = nominee;
  }

  openPFNomineeDetails(nominee: IPfNominee): void {
    const modalRef = this.modalService.open(PfNomineeDetailsModalComponent, { size: 'lg', backdrop: true, keyboard: false });
    modalRef.componentInstance.nominee = nominee;
  }

  getStatus(status: string): string {
    if (status === 'true') {
      return 'Approved';
    }
    return 'Not Approved';
  }

  redirectToNomineeReport(nomineeType: String, nomineeMaster: NomineeMaster): void {
    if (nomineeType === NomineeType.GRATUITY_FUND) {
      if (nomineeMaster.gfSharePercentage !== 100) {
        swalOnRejectionForNomineeReport();
      } else {
        sessionStorage.setItem('midRouteOfNomineeReport', `/nominee/summary/${nomineeMaster.pin}/view`);
        sessionStorage.setItem('midHeaderOfNomineeReport', `Nominee Details`);
        this.router.navigate(['/nominee/gf-form/nominee-report', nomineeMaster.id]);
      }
    } else if (nomineeType === NomineeType.GENERAL) {
      if (nomineeMaster.generalSharePercentage !== 100) {
        swalOnRejectionForNomineeReport();
      } else {
        sessionStorage.setItem('midRouteOfNomineeReport', `/nominee/summary/${nomineeMaster.pin}/view`);
        sessionStorage.setItem('midHeaderOfNomineeReport', `Nominee Details`);
        this.router.navigate(['/nominee/general-form/nominee-report', nomineeMaster.id]);
      }
    } else {
      if (nomineeMaster.pfSharePercentage !== 100) {
        swalOnRejectionForNomineeReport();
      } else {
        this.router.navigate(['nominee/pf-nominee/print', nomineeMaster.pin]);
        sessionStorage.setItem('midRouteOfNomineeReport', `/nominee/summary/${nomineeMaster.pin}/view`);
        sessionStorage.setItem('midHeaderOfNomineeReport', `Nominee Details`);
      }
    }
  }

  redirectToUpdatePfNominee(pfNominee: IPfNominee): void {
    this.router.navigate(['/pf-nominee', pfNominee.id, 'edit']);
    sessionStorage.setItem('midRouteOfNomineeReport', `/nominee/summary/${pfNominee.pin}/view`);
    sessionStorage.setItem('midHeaderOfNomineeReport', `Nominee Details`);
  }

  redirectToUpdateGeneralNominee(nominee1: INominee): void {
    this.router.navigate(['/nominee/general/edit/', nominee1.id]);
    sessionStorage.setItem('midRouteOfNomineeReport', `/nominee/summary/${nominee1.pin}/view`);
    sessionStorage.setItem('midHeaderOfNomineeReport', `Nominee Details`);
  }

  redirectToUpdateGFNominee(nominee1: INominee): void {
    this.router.navigate(['/nominee/gf/edit/', nominee1.id]);
    sessionStorage.setItem('midRouteOfNomineeReport', `/nominee/summary/${nominee1.pin}/view`);
    sessionStorage.setItem('midHeaderOfNomineeReport', `Nominee Details`);
  }

  delete(nominee: any): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.nomineeService.deleteCommon(nominee.id).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.loadEmployeeDetails(this.employeePin);
          },
          () => this.requestFailed()
        );
      }
    });
  }

  deletePFNominee(nominee: any): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.pfNomineeService.delete(nominee.id).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.loadEmployeeDetails(this.employeePin);
          },
          () => this.requestFailed()
        );
      }
    });
  }

  addressSlice(address: string): String {
    return address.slice(0, 25) + '...';
  }

  requestFailed(): void {
    swalOnRequestError();
  }

  approvedGeneralNominees(): void {
    for (let i = 0; i < this.generalNominees.length; i++) {
      this.selectedIdSet.add(this.generalNominees[i].id);
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => Number(value));

    swalConfirmationWithMessage('Did you receive the hard copy?').then(result => {
      if (result.isConfirmed) {
        this.isGeneralApproving = true;
        this.subscribeToSaveResponse(this.generalGfNomineeApprovalService.approveSelected(this.approvalDTO));
        this.approvalDTO.listOfIds = [];
      } else if (result.isDenied) {
        swalChangesNotSaved();
        this.approvalDTO.listOfIds = [];
      }
    });
  }

  approvedGFNominees(): void {
    for (let i = 0; i < this.gfNominees.length; i++) {
      this.selectedIdSet.add(this.gfNominees[i].id);
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => Number(value));

    swalConfirmationWithMessage('Did you receive the hard copy?').then(result => {
      if (result.isConfirmed) {
        this.isGFApproving = true;
        this.subscribeToSaveResponse(this.generalGfNomineeApprovalService.approveSelected(this.approvalDTO));
        this.approvalDTO.listOfIds = [];
      } else if (result.isDenied) {
        swalChangesNotSaved();
        this.approvalDTO.listOfIds = [];
      }
    });
  }

  approvedPFNominees(): void {
    for (let i = 0; i < this.pfNominees.length; i++) {
      this.selectedIdSet.add(this.pfNominees[i].id);
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => Number(value));

    swalConfirmationWithMessage('Did you receive the hard copy?').then(result => {
      if (result.isConfirmed) {
        this.isPFApproving = true;
        this.subscribeToSaveResponse(this.pfNomineeApprovalService.approveSelected(this.approvalDTO));
        this.approvalDTO.listOfIds = [];
      } else if (result.isDenied) {
        swalChangesNotSaved();
        this.approvalDTO.listOfIds = [];
      }
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<boolean>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }
  private onSaveSuccess(): void {
    if (this.isGeneralApproving) {
      swalSuccessWithMessage('Hard copy has received');
      this.nomineesMaster.isAllGeneralNomineeApproved = 'ALL_APPROVED';
      this.loadEmployeeDetails(this.employeePin);
    } else if (this.isGFApproving) {
      swalSuccessWithMessage('Hard copy has received');
      this.loadEmployeeDetails(this.employeePin);
      this.nomineesMaster.isAllGFNomineeApproved = 'ALL_APPROVED';
    } else if (this.isPFApproving) {
      swalSuccessWithMessage('Hard copy has received');
      this.nomineesMaster.isAllPfNomineeApproved = 'ALL_APPROVED';
      this.loadEmployeeDetails(this.employeePin);
    } else {
      swalForErrorWithMessage('Hard copy has not received');
    }
  }

  private onSaveError(): void {
    swalOnRequestError();
  }
}
