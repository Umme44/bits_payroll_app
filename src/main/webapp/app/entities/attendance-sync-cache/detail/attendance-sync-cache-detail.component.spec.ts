import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AttendanceSyncCacheDetailComponent } from './attendance-sync-cache-detail.component';

describe('AttendanceSyncCache Management Detail Component', () => {
  let comp: AttendanceSyncCacheDetailComponent;
  let fixture: ComponentFixture<AttendanceSyncCacheDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AttendanceSyncCacheDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ attendanceSyncCache: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AttendanceSyncCacheDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AttendanceSyncCacheDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load attendanceSyncCache on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.attendanceSyncCache).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
