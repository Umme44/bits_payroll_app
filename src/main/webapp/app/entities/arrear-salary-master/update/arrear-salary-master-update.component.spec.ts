import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ArrearSalaryMasterFormService } from './arrear-salary-master-form.service';
import { ArrearSalaryMasterService } from '../service/arrear-salary-master.service';
import { IArrearSalaryMaster } from '../arrear-salary-master.model';

import { ArrearSalaryMasterUpdateComponent } from './arrear-salary-master-update.component';

describe('ArrearSalaryMaster Management Update Component', () => {
  let comp: ArrearSalaryMasterUpdateComponent;
  let fixture: ComponentFixture<ArrearSalaryMasterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let arrearSalaryMasterFormService: ArrearSalaryMasterFormService;
  let arrearSalaryMasterService: ArrearSalaryMasterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ArrearSalaryMasterUpdateComponent],
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
      .overrideTemplate(ArrearSalaryMasterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArrearSalaryMasterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    arrearSalaryMasterFormService = TestBed.inject(ArrearSalaryMasterFormService);
    arrearSalaryMasterService = TestBed.inject(ArrearSalaryMasterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const arrearSalaryMaster: IArrearSalaryMaster = { id: 456 };

      activatedRoute.data = of({ arrearSalaryMaster });
      comp.ngOnInit();

      expect(comp.arrearSalaryMaster).toEqual(arrearSalaryMaster);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearSalaryMaster>>();
      const arrearSalaryMaster = { id: 123 };
      jest.spyOn(arrearSalaryMasterFormService, 'getArrearSalaryMaster').mockReturnValue(arrearSalaryMaster);
      jest.spyOn(arrearSalaryMasterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearSalaryMaster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arrearSalaryMaster }));
      saveSubject.complete();

      // THEN
      expect(arrearSalaryMasterFormService.getArrearSalaryMaster).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(arrearSalaryMasterService.update).toHaveBeenCalledWith(expect.objectContaining(arrearSalaryMaster));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearSalaryMaster>>();
      const arrearSalaryMaster = { id: 123 };
      jest.spyOn(arrearSalaryMasterFormService, 'getArrearSalaryMaster').mockReturnValue({ id: null });
      jest.spyOn(arrearSalaryMasterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearSalaryMaster: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arrearSalaryMaster }));
      saveSubject.complete();

      // THEN
      expect(arrearSalaryMasterFormService.getArrearSalaryMaster).toHaveBeenCalled();
      expect(arrearSalaryMasterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearSalaryMaster>>();
      const arrearSalaryMaster = { id: 123 };
      jest.spyOn(arrearSalaryMasterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearSalaryMaster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(arrearSalaryMasterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
