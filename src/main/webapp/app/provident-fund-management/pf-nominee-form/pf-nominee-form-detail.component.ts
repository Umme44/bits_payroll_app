import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IdentityType } from 'app/shared/model/enumerations/identity-type.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { IPfNominee } from '../../shared/legacy/legacy-model/pf-nominee.model';

@Component({
  selector: 'jhi-pf-nominee-form-detail',
  templateUrl: './pf-nominee-form-detail-polished.component.html',
})
export class PfNomineeFormDetailComponent implements OnInit {
  nominee?: IPfNominee;
  nomineeAge!: number;

  constructor(protected activatedRoute: ActivatedRoute, protected applicationConfigService: ApplicationConfigService) {}

  ngOnInit(): void {
    sessionStorage.setItem('selectedNomineeType', 'pf');
    this.activatedRoute.data.subscribe(({ pfNominee }) => {
      this.nominee = pfNominee;
      this.nomineeAge = pfNominee.age;
    });
  }

  getNomineeImage(id: number): String {
    const url = this.applicationConfigService.getEndpointFor('files/nominee-image/' + id);
    return url;
  }

  getUIFriendlyNomineeType(identityType: string): string {
    if (identityType === IdentityType.NID) return 'NID';
    else if (identityType === IdentityType.PASSPORT) return 'Passport';
    else if (identityType === IdentityType.BIRTH_REGISTRATION) return 'Birth Registration';
    else if (identityType === IdentityType.OTHER) return 'Other';
    else return 'Other';
  }

  previousState(): void {
    window.history.back();
  }

  getPfNomineeImage(pfNomineeId: number): String {
    const url = this.applicationConfigService.getEndpointFor('files/nominee-image/' + pfNomineeId);
    return url;
  }
}
