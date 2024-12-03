jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EmployeePinService } from '../service/employee-pin.service';

import { EmployeePinDeleteDialogComponent } from './employee-pin-delete-dialog.component';

describe('EmployeePin Management Delete Component', () => {
  let comp: EmployeePinDeleteDialogComponent;
  let fixture: ComponentFixture<EmployeePinDeleteDialogComponent>;
  let service: EmployeePinService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EmployeePinDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(EmployeePinDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeePinDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EmployeePinService);
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
