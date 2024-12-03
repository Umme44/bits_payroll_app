import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { IdentityType } from '../../../../shared/model/enumerations/identity-type.model';
import { INominee } from '../../nominee.model';

@Component({
  selector: 'jhi-general-nominee-modal',
  templateUrl: './general-nominee-details.modal.component.html',
})
export class GeneralNomineeDetailsModalComponent implements OnInit, OnDestroy {
  nominee!: INominee;

  constructor(protected activatedRoute: ActivatedRoute, protected activeModal: NgbActiveModal, private modalService: NgbModal) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {}

  previousState(): void {
    window.history.back();
  }

  getNomineeImage(id: number): String {
    const url = SERVER_API_URL + 'files/common/nominee-image/' + id;
    return url;
  }

  closeModal(): void {
    this.activeModal.dismiss('Cross click');
  }

  getUIFriendlyNomineeType(identityType: IdentityType): string {
    if (identityType === IdentityType.NID) return 'NID';
    else if (identityType === IdentityType.PASSPORT) return 'Passport';
    else if (identityType === IdentityType.BIRTH_REGISTRATION) return 'Birth Registration';
    else if (identityType === IdentityType.OTHER) return 'Other';
    else return 'Other';
  }
}
