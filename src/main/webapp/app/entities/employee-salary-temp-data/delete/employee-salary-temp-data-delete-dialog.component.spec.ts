jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EmployeeSalaryTempDataService } from '../service/employee-salary-temp-data.service';

import { EmployeeSalaryTempDataDeleteDialogComponent } from './employee-salary-temp-data-delete-dialog.component';

describe('EmployeeSalaryTempData Management Delete Component', () => {
  let comp: EmployeeSalaryTempDataDeleteDialogComponent;
  let fixture: ComponentFixture<EmployeeSalaryTempDataDeleteDialogComponent>;
  let service: EmployeeSalaryTempDataService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EmployeeSalaryTempDataDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(EmployeeSalaryTempDataDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeSalaryTempDataDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EmployeeSalaryTempDataService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
