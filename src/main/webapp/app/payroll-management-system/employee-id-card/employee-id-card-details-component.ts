import { Component, OnInit } from '@angular/core';
import { IEmployeeStaticFile } from '../../shared/model/employee-static-file.model';
import {EmployeeIdCardService} from "./employee-id-card.service";

@Component({
  selector: 'jhi-employee-id-card-details',
  templateUrl: './employee-id-card-details-component.html',
  styleUrls: ['./employee-id-card-details-component.scss'],
})
export class EmployeeIdCardComponent implements OnInit {
  employeeStaticFile!: IEmployeeStaticFile;

  constructor(protected employeeIdCardService: EmployeeIdCardService) {}

  ngOnInit(): void {
    this.employeeIdCardService.loadMyIDCard().subscribe(res => {
      this.employeeStaticFile = res.body!;
    });
  }

  printIdCard(): void {
    window.print();
  }
}
