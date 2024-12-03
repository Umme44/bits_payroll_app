import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IPfGfStatement } from '../../../shared/model/pf-gf/pf-gf-statement.model';
import { FinalSettlementService } from '../service/final-settlement.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-pf-gf-statement',
  templateUrl: './pf-gf-statement.component.html',
  styleUrls: ['./pf-gf-statement.component.scss'],
})
export class PfGfStatementComponent implements OnInit {
  pfGfStatement!: IPfGfStatement;
  today = dayjs();
  organizationFullName = '';
  constructor(private finalSettlementService: FinalSettlementService, protected activatedRoute: ActivatedRoute) {
    if (sessionStorage.getItem('organizationShortName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }

  ngOnInit(): void {
    const employeeId = this.activatedRoute.snapshot.params['employeeId'];
    if (employeeId != null) {
      this.finalSettlementService.getPfGfStatement(employeeId).subscribe(response => {
        this.pfGfStatement = response;
      });
    }
  }

  previousState(): void {
    window.history.back();
  }

  public downloadAsPDF(): void {
    window.print();
  }
}
