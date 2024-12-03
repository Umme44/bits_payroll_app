import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IncomeTaxChallanFormService } from './income-tax-challan-form.service';
import { IncomeTaxChallanService } from '../service/income-tax-challan.service';
import { IIncomeTaxChallan } from '../income-tax-challan.model';
import { IAitConfig } from 'app/entities/ait-config/ait-config.model';
import { AitConfigService } from 'app/entities/ait-config/service/ait-config.service';

import { IncomeTaxChallanUpdateComponent } from './income-tax-challan-update.component';

describe('IncomeTaxChallan Management Update Component', () => {
  let comp: IncomeTaxChallanUpdateComponent;
  let fixture: ComponentFixture<IncomeTaxChallanUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let incomeTaxChallanFormService: IncomeTaxChallanFormService;
  let incomeTaxChallanService: IncomeTaxChallanService;
  let aitConfigService: AitConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IncomeTaxChallanUpdateComponent],
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
      .overrideTemplate(IncomeTaxChallanUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncomeTaxChallanUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incomeTaxChallanFormService = TestBed.inject(IncomeTaxChallanFormService);
    incomeTaxChallanService = TestBed.inject(IncomeTaxChallanService);
    aitConfigService = TestBed.inject(AitConfigService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AitConfig query and add missing value', () => {
      const incomeTaxChallan: IIncomeTaxChallan = { id: 456 };
      const aitConfig: IAitConfig = { id: 10910 };
      incomeTaxChallan.aitConfig = aitConfig;

      const aitConfigCollection: IAitConfig[] = [{ id: 98799 }];
      jest.spyOn(aitConfigService, 'query').mockReturnValue(of(new HttpResponse({ body: aitConfigCollection })));
      const additionalAitConfigs = [aitConfig];
      const expectedCollection: IAitConfig[] = [...additionalAitConfigs, ...aitConfigCollection];
      jest.spyOn(aitConfigService, 'addAitConfigToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ incomeTaxChallan });
      comp.ngOnInit();

      expect(aitConfigService.query).toHaveBeenCalled();
      expect(aitConfigService.addAitConfigToCollectionIfMissing).toHaveBeenCalledWith(
        aitConfigCollection,
        ...additionalAitConfigs.map(expect.objectContaining)
      );
      expect(comp.aitConfigsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const incomeTaxChallan: IIncomeTaxChallan = { id: 456 };
      const aitConfig: IAitConfig = { id: 31189 };
      incomeTaxChallan.aitConfig = aitConfig;

      activatedRoute.data = of({ incomeTaxChallan });
      comp.ngOnInit();

      expect(comp.aitConfigsSharedCollection).toContain(aitConfig);
      expect(comp.incomeTaxChallan).toEqual(incomeTaxChallan);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncomeTaxChallan>>();
      const incomeTaxChallan = { id: 123 };
      jest.spyOn(incomeTaxChallanFormService, 'getIncomeTaxChallan').mockReturnValue(incomeTaxChallan);
      jest.spyOn(incomeTaxChallanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeTaxChallan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomeTaxChallan }));
      saveSubject.complete();

      // THEN
      expect(incomeTaxChallanFormService.getIncomeTaxChallan).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(incomeTaxChallanService.update).toHaveBeenCalledWith(expect.objectContaining(incomeTaxChallan));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncomeTaxChallan>>();
      const incomeTaxChallan = { id: 123 };
      jest.spyOn(incomeTaxChallanFormService, 'getIncomeTaxChallan').mockReturnValue({ id: null });
      jest.spyOn(incomeTaxChallanService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeTaxChallan: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: incomeTaxChallan }));
      saveSubject.complete();

      // THEN
      expect(incomeTaxChallanFormService.getIncomeTaxChallan).toHaveBeenCalled();
      expect(incomeTaxChallanService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIncomeTaxChallan>>();
      const incomeTaxChallan = { id: 123 };
      jest.spyOn(incomeTaxChallanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ incomeTaxChallan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incomeTaxChallanService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAitConfig', () => {
      it('Should forward to aitConfigService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(aitConfigService, 'compareAitConfig');
        comp.compareAitConfig(entity, entity2);
        expect(aitConfigService.compareAitConfig).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
