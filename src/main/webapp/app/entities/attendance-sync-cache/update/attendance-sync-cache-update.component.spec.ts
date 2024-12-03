import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AttendanceSyncCacheFormService } from './attendance-sync-cache-form.service';
import { AttendanceSyncCacheService } from '../service/attendance-sync-cache.service';
import { IAttendanceSyncCache } from '../attendance-sync-cache.model';

import { AttendanceSyncCacheUpdateComponent } from './attendance-sync-cache-update.component';

describe('AttendanceSyncCache Management Update Component', () => {
  let comp: AttendanceSyncCacheUpdateComponent;
  let fixture: ComponentFixture<AttendanceSyncCacheUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attendanceSyncCacheFormService: AttendanceSyncCacheFormService;
  let attendanceSyncCacheService: AttendanceSyncCacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AttendanceSyncCacheUpdateComponent],
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
      .overrideTemplate(AttendanceSyncCacheUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttendanceSyncCacheUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attendanceSyncCacheFormService = TestBed.inject(AttendanceSyncCacheFormService);
    attendanceSyncCacheService = TestBed.inject(AttendanceSyncCacheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const attendanceSyncCache: IAttendanceSyncCache = { id: 456 };

      activatedRoute.data = of({ attendanceSyncCache });
      comp.ngOnInit();

      expect(comp.attendanceSyncCache).toEqual(attendanceSyncCache);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendanceSyncCache>>();
      const attendanceSyncCache = { id: 123 };
      jest.spyOn(attendanceSyncCacheFormService, 'getAttendanceSyncCache').mockReturnValue(attendanceSyncCache);
      jest.spyOn(attendanceSyncCacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendanceSyncCache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendanceSyncCache }));
      saveSubject.complete();

      // THEN
      expect(attendanceSyncCacheFormService.getAttendanceSyncCache).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attendanceSyncCacheService.update).toHaveBeenCalledWith(expect.objectContaining(attendanceSyncCache));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendanceSyncCache>>();
      const attendanceSyncCache = { id: 123 };
      jest.spyOn(attendanceSyncCacheFormService, 'getAttendanceSyncCache').mockReturnValue({ id: null });
      jest.spyOn(attendanceSyncCacheService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendanceSyncCache: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendanceSyncCache }));
      saveSubject.complete();

      // THEN
      expect(attendanceSyncCacheFormService.getAttendanceSyncCache).toHaveBeenCalled();
      expect(attendanceSyncCacheService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttendanceSyncCache>>();
      const attendanceSyncCache = { id: 123 };
      jest.spyOn(attendanceSyncCacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendanceSyncCache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attendanceSyncCacheService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
