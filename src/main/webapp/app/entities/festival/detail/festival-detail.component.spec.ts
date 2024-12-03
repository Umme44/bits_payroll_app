import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FestivalDetailComponent } from './festival-detail.component';

describe('Festival Management Detail Component', () => {
  let comp: FestivalDetailComponent;
  let fixture: ComponentFixture<FestivalDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FestivalDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ festival: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FestivalDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FestivalDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load festival on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.festival).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
