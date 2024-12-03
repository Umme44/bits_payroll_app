import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEmployeeNOC } from '../../model/employee-noc.model';

@Component({
  selector: 'jhi-employee-noc-print-format',
  templateUrl: './employee-noc-print-format.component.html',
  styleUrls: ['./employee-noc-print-format.component.scss'],
})
export class EmployeeNOCPrintFormatComponent implements OnInit {
  employeeNOC: IEmployeeNOC | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeNOC }) => (this.employeeNOC = employeeNOC));
  }

  previousState(): void {
    window.history.back();
  }

  print(): void {
    window.print();
  }
}
