import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FinalSettlementService } from './final-settlement.service';
import { IPfStatement } from '../../shared/model/pf/pf-statement.model';

@Component({
  selector: 'jhi-pf-statement-modal',
  templateUrl: './pf-statement-modal.component.html',
  styleUrls: ['./pf-statement-modal.component.scss'],
})
export class PfStatementModalComponent implements OnInit {
  pfStatement!: IPfStatement;

  @Input()
  employeeId!: number;

  @ViewChild('pdfPfStatement', { static: false })
  pdfPfStatement: ElementRef | undefined;

  @Output() passEntry: EventEmitter<boolean> = new EventEmitter();

  constructor(private activeModal: NgbActiveModal, private finalSettlementService: FinalSettlementService) {}

  ngOnInit(): void {
    this.finalSettlementService.getPfStatement(this.employeeId).subscribe(response => {
      this.pfStatement = response;
    });
  }

  monthNameLimitTo3(month: string): string {
    month = month.slice(0, 3).toLowerCase();
    month = month[0].toUpperCase() + month[1] + month[2];
    return month;
  }

  yearlyTotalContribution(totalEmployeeContributionInYear: number, totalEmployerContributionInYear: number): number {
    return totalEmployeeContributionInYear + totalEmployerContributionInYear;
  }

  public downloadAsPDF(): void {
    window.print();
  }

  cancel(): void {
    this.passEntry.emit(false);
    this.activeModal.dismiss();
  }
}
