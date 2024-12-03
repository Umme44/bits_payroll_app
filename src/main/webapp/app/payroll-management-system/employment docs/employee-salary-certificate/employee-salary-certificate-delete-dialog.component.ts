import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeSalaryCertificateService } from './employee-salary-certificate.service';
import { ISalaryCertificate } from '../../../shared/legacy/legacy-model/salary-certificate.model';
import { EventManager } from '@angular/platform-browser';

@Component({
  templateUrl: './employee-salary-certificate-delete-dialog.component.html',
})
export class EmployeeSalaryCertificateDeleteDialogComponent {
  salaryCertificate?: ISalaryCertificate;

  constructor(
    protected employeeSalaryCertificateService: EmployeeSalaryCertificateService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeSalaryCertificateService.delete(id).subscribe(() => {
      //this.eventManager.broadcast('employeeNOCListModification');
      this.activeModal.close();
    });
  }
}
