import { Component, OnDestroy, OnInit } from '@angular/core';
import { NomineeType } from 'app/shared/model/enumerations/nominee-type.model';
import { INomineeEligibility } from '../../shared/model/nominee-eligibility';
import { NomineeService } from '../../shared/legacy/legacy-service/nominee.service';

@Component({
  selector: 'jhi-nominee-master-common',
  templateUrl: './nominee-master-common.component.html',
  styleUrls: ['nominee-master-common.component.scss'],
})
export class NomineeMasterCommonComponent implements OnInit, OnDestroy {
  nomineeTypeGF = NomineeType.GRATUITY_FUND;
  nomineeTypeGeneral = NomineeType.GENERAL;
  activeElementName = 'general';
  active = 1;
  total = 3;

  nomineeEligibility: INomineeEligibility = {
    eligibleForGeneral: true,
    eligibleForGf: false,
    eligibleForPf: false,
  };

  constructor(private nomineeService: NomineeService) {}

  ngOnInit(): void {
    if (sessionStorage.getItem('selectedNomineeType')) {
      this.activeElementName = sessionStorage.getItem('selectedNomineeType')!;
    }
    this.nomineeService.checkEligibility().subscribe(res => (this.nomineeEligibility = res.body!));
  }

  ngOnDestroy(): void {
    sessionStorage.removeItem('selectedNomineeType');
  }

  renderNomineePage(elementName: string): void {
    this.activeElementName = elementName;
  }

  nextTab(): void {
    if (this.active !== this.total) this.active = this.active + 1;
  }

  previousTab(): void {
    if (this.active > 1) {
      this.active = (this.active - 1) % this.total;
    }
  }
}
