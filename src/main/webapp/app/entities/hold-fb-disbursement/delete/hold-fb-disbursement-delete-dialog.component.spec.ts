jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { HoldFbDisbursementService } from '../service/hold-fb-disbursement.service';

import { HoldFbDisbursementDeleteDialogComponent } from './hold-fb-disbursement-delete-dialog.component';

describe('HoldFbDisbursement Management Delete Component', () => {
  let comp: HoldFbDisbursementDeleteDialogComponent;
  let fixture: ComponentFixture<HoldFbDisbursementDeleteDialogComponent>;
  let service: HoldFbDisbursementService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HoldFbDisbursementDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(HoldFbDisbursementDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HoldFbDisbursementDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HoldFbDisbursementService);
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
