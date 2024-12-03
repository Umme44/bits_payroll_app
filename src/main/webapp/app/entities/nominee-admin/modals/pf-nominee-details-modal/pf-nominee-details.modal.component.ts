import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { IPfNominee } from '../../pf-nominee.model';

@Component({
  selector: 'jhi-pf-nominee-modal',
  templateUrl: './pf-nominee-details.modal.component.html',
})
export class PfNomineeDetailsModalComponent implements OnInit, OnDestroy {
  nominee!: IPfNominee;

  constructor(protected activatedRoute: ActivatedRoute, protected activeModal: NgbActiveModal, private modalService: NgbModal) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {}

  previousState(): void {
    window.history.back();
  }

  getNomineeImage(id: number): String {
    const url = SERVER_API_URL + 'files/nominee-image/' + id;
    return url;
  }

  closeModal(): void {
    this.activeModal.dismiss('Cross click');
  }

  getUIFriendlyNomineeType(identityType: string): string {
    if (identityType === 'NID') return 'NID';
    else if (identityType === 'Passport') return 'Passport';
    else if (identityType === 'Birth Registration') return 'Birth Registration';
    else if (identityType === 'Other') return 'Other';
    else return 'Other';
  }
}
