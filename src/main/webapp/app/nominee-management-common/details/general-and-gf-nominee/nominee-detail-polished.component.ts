import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NomineeType } from 'app/shared/model/enumerations/nominee-type.model';
import { INominee } from '../../../entities/nominee-admin/nominee.model';
import { IdentityType } from '../../../shared/model/enumerations/identity-type.model';

@Component({
  selector: 'jhi-nominee-detail',
  templateUrl: './nominee-detail-polished.component.html',
})
export class NomineeDetailPolishedComponent implements OnInit {
  nominee!: INominee;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nominee }) => {
      this.nominee = nominee;
      this.nominee.nomineeType === NomineeType.GENERAL
        ? sessionStorage.setItem('selectedNomineeType', 'general')
        : sessionStorage.setItem('selectedNomineeType', 'gf');
    });
  }

  previousState(): void {
    window.history.back();
  }

  getNomineeImage(id: number): String {
    const url = SERVER_API_URL + 'files/common/nominee-image/' + id;
    return url;
  }

  getUIFriendlyNomineeType(identityType: IdentityType): string {
    if (identityType === IdentityType.NID) return 'NID';
    else if (identityType === IdentityType.PASSPORT) return 'Passport';
    else if (identityType === IdentityType.BIRTH_REGISTRATION) return 'Birth Registration';
    else if (identityType === IdentityType.OTHER) return 'Other';
    else return 'Other';
  }
}
