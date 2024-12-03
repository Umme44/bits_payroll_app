import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PfAccountFormService } from './pf-account-form.service';
import { PfAccountService } from '../service/pf-account.service';
import { IPfAccount } from '../pf-account.model';

import { PfAccountUpdateComponent } from './pf-account-update.component';

describe('PfAccount Management Update Component', () => {
  let comp: PfAccountUpdateComponent;
  let fixture: ComponentFixture<PfAccountUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pfAccountFormService: PfAccountFormService;
  let pfAccountService: PfAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PfAccountUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PfAccountUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PfAccountUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pfAccountFormService = TestBed.inject(PfAccountFormService);
    pfAccountService = TestBed.inject(PfAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pfAccount: IPfAccount = { id: 456 };

      activatedRoute.data = of({ pfAccount });
      comp.ngOnInit();

      expect(comp.pfAccount).toEqual(pfAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfAccount>>();
      const pfAccount = { id: 123 };
      jest.spyOn(pfAccountFormService, 'getPfAccount').mockReturnValue(pfAccount);
      jest.spyOn(pfAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfAccount }));
      saveSubject.complete();

      // THEN
      expect(pfAccountFormService.getPfAccount).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pfAccountService.update).toHaveBeenCalledWith(expect.objectContaining(pfAccount));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfAccount>>();
      const pfAccount = { id: 123 };
      jest.spyOn(pfAccountFormService, 'getPfAccount').mockReturnValue({ id: null });
      jest.spyOn(pfAccountService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfAccount: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfAccount }));
      saveSubject.complete();

      // THEN
      expect(pfAccountFormService.getPfAccount).toHaveBeenCalled();
      expect(pfAccountService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfAccount>>();
      const pfAccount = { id: 123 };
      jest.spyOn(pfAccountService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfAccount });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pfAccountService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
