import { Component, OnInit } from '@angular/core';

import { Subscription } from 'rxjs';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { swalOnDeleteConfirmation, swalOnDeleteSuccess, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import { IReferences } from '../../../references/references.model';
import { IEmployee } from '../../employee-custom.model';
import { ReferencesService } from '../../../references/service/references.service';

@Component({
  selector: 'jhi-employee-references-modal',
  templateUrl: './employee-references.modal.component.html',
})
export class EmployeeReferencesModalComponent implements OnInit {
  employeeId!: number;
  idForUpdate = -1;

  references!: IReferences[];
  eventSubscriber?: Subscription;
  employee!: IEmployee;

  constructor(private referencesService: ReferencesService, private modalService: NgbModal, private activeModal: NgbActiveModal) {}

  loadAll(): void {
    this.referencesService.queryByEmployeeId(this.employeeId).subscribe(res => (this.references = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
  }

  delete(references: IReferences): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.referencesService.delete(references?.id!).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.loadAll();
          },
          () => swalOnRequestError()
        );
      }
    });
  }

  dismiss(): void {
    this.activeModal.dismiss('Cross click');
  }

  populateEditForm(id: any): void {
    this.idForUpdate = id;
  }
}
