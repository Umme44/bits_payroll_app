jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IncomeTaxChallanService } from '../service/income-tax-challan.service';

import { IncomeTaxChallanDeleteDialogComponent } from './income-tax-challan-delete-dialog.component';

describe('IncomeTaxChallan Management Delete Component', () => {
  let comp: IncomeTaxChallanDeleteDialogComponent;
  let fixture: ComponentFixture<IncomeTaxChallanDeleteDialogComponent>;
  let service: IncomeTaxChallanService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IncomeTaxChallanDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(IncomeTaxChallanDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IncomeTaxChallanDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IncomeTaxChallanService);
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
